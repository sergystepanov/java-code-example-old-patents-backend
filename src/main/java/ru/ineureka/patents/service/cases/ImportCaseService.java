package ru.ineureka.patents.service.cases;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.dto.patent.PropertyCaseRecordDto;
import ru.ineureka.patents.dto.patent.PropertyDto;
import ru.ineureka.patents.dto.patent.PropertyOwnerDto;
import ru.ineureka.patents.dto.type.PropertyTypeMap;
import ru.ineureka.patents.persistence.client.ClientImportTemplate;
import ru.ineureka.patents.persistence.map.IdNameMap;
import ru.ineureka.patents.persistence.office.PatentOffice;
import ru.ineureka.patents.persistence.office.PatentOfficeFee;
import ru.ineureka.patents.persistence.office.PatentOfficeNameMap;
import ru.ineureka.patents.persistence.property.PropertyType;
import ru.ineureka.patents.reader.excel.ExcelTable;
import ru.ineureka.patents.service.PatentOfficeProxy;
import ru.ineureka.patents.service.cases.client.CaseMessageDto;
import ru.ineureka.patents.service.cases.client.ClientCaseDto;
import ru.ineureka.patents.service.cases.client.ClientCaseRecordDto;
import ru.ineureka.patents.service.cases.exception.HeaderException;
import ru.ineureka.patents.service.checker.PropertyDataChecker;
import ru.ineureka.patents.service.guesser.PropertyDataGuesser;
import ru.ineureka.patents.service.office.OfficeService;
import ru.ineureka.patents.service.property.PropertyService;
import ru.ineureka.patents.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static ru.ineureka.patents.service.cases.Fields.*;

/**
 * Object which represents a client's case for maintenance.
 *
 * @since 3.0.0
 */
@Service
public final class ImportCaseService {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final PatentOfficeProxy patentOfficeProxy;
    private final OfficeService officeService;
    private final PropertyService propertyService;

    private final ObjectMapper mapper = new ObjectMapper();

    private ImportCaseMessages messages;

    public ImportCaseService(PatentOfficeProxy patentOfficeProxy,
                             OfficeService officeService,
                             PropertyService propertyService) {
        this.patentOfficeProxy = patentOfficeProxy;
        this.officeService = officeService;
        this.propertyService = propertyService;
    }

    /**
     * Gathers all the data for a case.
     *
     * @return A {@code ClientCaseDto} object with data.
     * @since 3.0.0
     */
    public ClientCaseDto process(ExcelTable table, ClientImportTemplate template) {
        final List<Map<String, String>> records = new ArrayList<>();
        // Refresh messages for un-static purposes
        this.messages = new ImportCaseMessages();

        // Read data from the database
        final var fees = officeService.getFees();
        final var propertyTypes = propertyService.getAllTypes();

        try {
            final Map<String, List<String>> fieldMap = mapper.readValue(
                    template.getFieldMap(), new TypeReference<>() {
                    });
            final Map<String, List<String>> typeMap = mapper.readValue(
                    template.getPatentTypeMap(), new TypeReference<>() {
                    });

            // Apply the template to the normalized header
            final Map<Integer, String> header = table.getNormalizedHeader().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> findValue(e.getValue(), fieldMap)));
            // Check the header
            logger.atFine().log(header.values().toString());
            if (!header.values().containsAll(requiredFields)) {
                throw new HeaderException();
            }

            // Name all the cells by its indexes
            // Cache table data
            final Map<Integer, Map<Integer, String>> data = table.getData();

            final var guesser = new PropertyDataGuesser();
            final var checker = new PropertyDataChecker();

            for (var rowNum : data.keySet()) {
                Map<String, String> record = data.get(rowNum).entrySet().stream().collect(Collectors.toMap(
                        e -> header.get(e.getKey()), e -> {
                            final String fieldName = header.get(e.getKey());
                            final String fieldValue = e.getValue();

                            // Transform values
                            // The property's type to a corresponding letter
                            if (fieldName.equals(TYPE.toString())) {
                                return findValue(fieldValue, typeMap);
                            }

                            // Patent date fields into Ymd formatted date
                            if (valuesWithDates.contains(fieldName)) {
                                return guesser.getDate(fieldValue);
                            }

                            return fieldValue;
                        }
                ));

                record.put(ID.toString(), String.valueOf(rowNum));
                record.put(OFFICE.toString(), guesser.getRegistry(record));
                record.put(OPEN_LICENSE.toString(), findOpenLicense(record));

                // Cut fee if open license
                final BigDecimal fee = findFee(fees, record, value -> {
                    if (getValue(record, OPEN_LICENSE).equals("1")) {
                        final var amount = value.compareTo(BigDecimal.ZERO) == 0 ?
                                value.divide(BigDecimal.valueOf(2), RoundingMode.UP) : BigDecimal.ZERO;
                        messages.addWarning(rowNum,
                                "Оплата патента по заявке №" + getValue(record, APPLICATION_NUMBER) +
                                        " была уменьшена в 2 раза до " + amount + ".");
                        return amount;
                    }

                    return value;
                });

                record.put(OFFICE_FEE.toString(), formatMoneyValue(fee));

//                var property = patentOfficeProxy.getInput(new PropertyRequest(
//                        record.getOrDefault(OFFICE.toString(), "x"),
//                        getValue(record, TYPE),
//                        getValue(record, REGISTRATION_NUMBER).isEmpty() ?
//                                getValue(record, APPLICATION_NUMBER) :
//                                getValue(record, REGISTRATION_NUMBER)
//                ));
//
//                record.put(CACHED.toString(), String.valueOf(property.isCached));

                // Search for property type id by string value
                final PropertyType type = propertyTypes.stream()
                        .filter(t -> Objects.equals(t.getCode(), getValue(record, TYPE).toLowerCase()))
                        .findFirst()
                        .orElseGet(() -> {
                            this.messages.addErrorForField(
                                    Long.parseLong(record.get(ID.toString())), TYPE.toString(),
                                    "Не определён тип.");

                            return null;
                        });
                if (type != null) {
                    record.put(TYPE_ID.toString(), String.valueOf(type.getId()));
                }

                // Expiry and grace
                // The grace period
                if (getValue(record, GRACE_PERIOD).isEmpty()) {
                    record.put(GRACE_PERIOD.toString(), guesser.getGracePeriod(record));
                }

                if (getValue(record, EXPIRY_DATE).isEmpty() && type != null) {
                    record.put(EXPIRY_DATE.toString(), guesser.getExpiryDate(record, type.getDurationYears()));
                }

                final List<CaseMessageDto> checkerMessages = new ArrayList<>();
                // Check the annuity periods
                checkerMessages.addAll(checker.checkAnnuity(record));
                // Check the already paid records
                checkerMessages.addAll(checker.checkPaid(record));
                messages.addAll(checkerMessages);

                // Guess the annuity period
                final String[] annuityPeriod = guesser.getAnnuityPeriod(record);
                record.put(PAYMENT_ANNUITY_YEARS.toString(), annuityPeriod[1]);
                if (!record.getOrDefault(PAYMENT_ANNUITY.toString(), "").equals(annuityPeriod[0])) {
                    record.replace(PAYMENT_ANNUITY.toString(), annuityPeriod[0]);
                }

                records.add(
                        // Sort
                        record.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                        (oldValue, newValue) -> oldValue, LinkedHashMap::new))
                );

                // Check if mandatory cells are not empty
                final List<String> empty = requiredFields.stream()
                        .filter(field -> record.get(field).isEmpty())
                        .collect(Collectors.toList());

                if (!empty.isEmpty()) {
                    messages.addWarning(rowNum,
                            "В строке " + rowNum + " нет данных для обязательных столбцов: " + empty);
                }
            }

            // Check the duplicate records
            messages.addAll(checker.checkDuplicates(records));
        } catch (IOException e) {
            logger.atSevere().withCause(e);
            messages.addError(0, "Ошибка шаблона.");
        } catch (HeaderException he) {
            logger.atSevere().withCause(he).log("None of mandatory fields are present");
            messages.addError(0, "В файле отсутствуют столбцы: " + requiredFields);
        }

        return assembleCase(records, messages.getMessages());
    }

    private String findOpenLicense(Map<String, String> record) {
        return propertyService.isOpenLicensed(
                IdNameMap.getByName(getValue(record, OFFICE), PatentOfficeNameMap.values),
                IdNameMap.getByName(getValue(record, TYPE), PropertyTypeMap.values),
                getValue(record, APPLICATION_NUMBER)
        );
    }

    /**
     * Finds a key which is mapped to a list of values.
     *
     * @param name   A value to find.
     * @param values Where to find.
     * @return A key of the found value or initial name in the other case.
     * @since 3.0.0
     */
    private String findValue(String name, Map<String, List<String>> values) {
        return values.entrySet().stream()
                .filter(e -> e.getValue().contains(name))
                .map(Map.Entry::getKey)
                .findFirst().orElse(name);
    }

    private BigDecimal findFee(List<PatentOfficeFee> fees, Map<String, String> record, FieldAction<BigDecimal> after) {
        return after.action(
                fees.stream()
                        .filter(
                                x -> IdNameMap.getById(x.getPatentOffice(), PatentOfficeNameMap.values)
                                        .equals(record.get(OFFICE.toString())) &&
                                        IdNameMap.getById(x.getPropertyType(), PropertyTypeMap.values)
                                                .equals(record.get(TYPE.toString())) &&
                                        String.valueOf(x.getYear()).equals(
                                                record.get(PAYMENT_ANNUITY.toString())) &&
                                        x.getCountry().equals(record.get(NATION.toString()))
                        )
                        .findFirst()
                        .map(PatentOfficeFee::getCost).orElse(BigDecimal.ZERO)
        );
    }

    private ClientCaseDto assembleCase(List<Map<String, String>> rows, List<CaseMessageDto> mess) {
        final List<ClientCaseRecordDto> records = new ArrayList<>();

        // Get all one-to-many from the db
        final var propertyOffices = officeService.getAllOffices();

        for (Map<String, String> row : rows) {
            // Search for property office id by string value
            final long officeId = propertyOffices.stream()
                    .filter(o -> Objects.equals(o.getCode(), getValue(row, OFFICE)))
                    .findFirst()
                    .map(PatentOffice::getId)
                    .orElseGet(() -> {
                        this.messages.addErrorForField(
                                Long.parseLong(row.get(ID.toString())),
                                OFFICE.toString(), "Не определён реестр.");

                        return 0L;
                    });

            final String typeId = getValue(row, TYPE_ID);

            final var property = new PropertyDto(0L,
                    getValue(row, REGISTRATION_NUMBER),
                    typeId.isEmpty() ? 0 : Long.parseLong(typeId),
                    officeId,
                    getValue(row, NATION),
                    getValue(row, APPLICATION_NUMBER),
                    "",
                    getValue(row, APPLICATION_DATE),
                    getValue(row, GRANT_DATE),
                    getValue(row, GRACE_PERIOD),
                    getValue(row, EXPIRY_DATE),
                    getValue(row, DESCRIPTION),
                    getValue(row, "status"),
                    Objects.equals(getValue(row, OPEN_LICENSE), "1")
            );

            final String owners = getValue(row, PROPRIETORS);
            if (!owners.isEmpty()) {
                property.setOwners(Collections.singletonList(
                        new PropertyOwnerDto(0L, owners, "", Collections.emptyList()))
                );
            }

            final PropertyCaseRecordDto caseRecord = new PropertyCaseRecordDto(
                    0L, 0L, 0L, false, officeId,
                    getValue(row, NATION),
                    getValue(row, DUE_DATE),
                    getNumberValue(row, PAYMENT_ANNUITY),
                    getNumberValueOr(row, PAYMENT_ANNUITY_YEARS, 1),
                    getMoneyValue(row, PAYMENT_AMOUNT),
                    getMoneyValue(row, PAYMENT_LATE_FINE),
                    1.00,
                    0.00,
                    0, 0, false, false
            );

            final ClientCaseRecordDto record = new ClientCaseRecordDto(
                    Long.parseLong(row.getOrDefault(ID.toString(), "0")),
                    property,
                    Objects.equals("true", getValue(row, CACHED)),
                    caseRecord
            );

            records.add(record);
        }

        return new ClientCaseDto(records, mess);
    }

    private static String getValue(Map<String, String> row, Fields field) {
        return row.getOrDefault(field.toString(), "");
    }

    private int getNumberValue(Map<String, String> row, Fields field) {
        return this.getNumberValueOr(row, field, 0);
    }

    private int getNumberValueOr(Map<String, String> row, Fields field, int defaultValue) {
        final String field_ = field.toString();
        final String value = row.get(field_);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(StringUtils.notNumeric.matcher(value).replaceAll(""));
        } catch (NumberFormatException e) {
            this.messages.addErrorForField(Long.parseLong(row.get(ID.toString())),
                    field_, "Поле " + field_ + " со значением: " + value + " должно быть числом.");
        }

        return defaultValue;
    }

    private double getMoneyValue(Map<String, String> row, Fields field) {
        final String field_ = field.toString();
        final String value = row.getOrDefault(field_, "0.00");

        try {
            //return Double.valueOf(value.replaceAll("[^\\d.]", "").replaceAll(",", "."));
            //return Double.parseDouble(StringUtils.replace(StringUtils.replace(value, " ", ""), ",", "."));
            return Double.parseDouble(StringUtils.notNumeric.matcher(value)
                    .replaceAll("")
                    .replace(",", "."));
        } catch (NumberFormatException e) {
            this.messages.addErrorForField(Long.parseLong(row.get(ID.toString())),
                    field_, "Поле " + field_ + " со значением: " + value + " должно быть числом.");
        }

        return 0.00;
    }

    private String getValue(Map<String, String> row, String field) {
        return row.getOrDefault(field, "");
    }

    /**
     * Returns a value with a currency format.
     *
     * @param value The value to format.
     * @since 3.0.0
     */
    private String formatMoneyValue(BigDecimal value) {
        return String.format(Locale.ENGLISH, "%.2f", value);
    }
}
