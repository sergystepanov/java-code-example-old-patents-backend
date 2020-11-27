package ru.ineureka.patents.office.org.eapo;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import ru.ineureka.patents.office.Office;
import ru.ineureka.patents.office.PatentOffice;
import ru.ineureka.patents.office.dto.PatentExtendedDto;
import ru.ineureka.patents.office.dto.PatentFeeDto;
import ru.ineureka.patents.office.exception.OfficeError;
import ru.ineureka.patents.office.exception.PatentOfficeException;
import ru.ineureka.patents.property.Inid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Eurasian Patent Organization (EAPO) patent office document reader.
 * <p>
 * Originally was created on 25.12.12 2:24.
 * </p>
 *
 * @since 1.0.0
 */
public final class Eapo extends PatentOffice {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static final String URL = "https://www.eapo.org";
    public static final String ENCODING = "windows-1251";
    private final Map<String, String> cache = new HashMap<>();

    public Eapo(byte[] bytes) throws PatentOfficeException {
        super(bytes);
    }

    /**
     * Checks if the document has all necessary data for processing.
     *
     * @return True if document is ok.
     * @since 1.0.0
     */
    @Override
    protected boolean isDocumentAlrightAlrightAlright() {
        final boolean found = Pattern.compile(">\\(" + Inid.APPLICATION_NUMBER + "\\) .*<").matcher(text).find();
        logValue("correctness document marker (INID 21)", found ? "+" : "-");

        return found;
    }

    @Override
    public String getEncoding() {
        return "windows-1251";
    }

    @Override
    protected FluentLogger getLogger() {
        return logger;
    }

    public PatentExtendedDto getPatent() throws PatentOfficeException {
        final long start = System.currentTimeMillis();

        isDocumentAlrightAlrightAlright();

        PatentExtendedDto.PatentExtendedDtoBuilder patent = PatentExtendedDto.builder();

        try {
            final String propertyRegistrationNumber = findPatentNumber();
            final String propertyApplicationNumber = findPatentApplicationNumber();

            patent
                    .registry(Office.EAPO)
                    .grant_no(
                            // In case if were provided only an application for the property
                            propertyRegistrationNumber.equals(propertyApplicationNumber) ?
                                    "" : propertyRegistrationNumber)
                    .grantDate(findPatentPubDate())
                    .startDate(findPatentStartDate())
                    .application_no(propertyApplicationNumber)
                    .applicationDate(findPatentApplicationDate())
                    .nation(findPatentCountry())
                    .proprietors(findPatentProprietors())
                    .status(findPatentStatus())
                    .corrections(findCorrections())
                    .corrections_text(findCorrectionsText());

            findPatentFee().ifPresent(fee -> {
                if (fee.getCountries().contains("RU")) {
                    patent
                            .fee_message(fee.getMessage())
                            .fee_year(fee.getAnnuity())
                            .annuity(fee.getAnnuity())
                            .feeFromDate(fee.getStartDate())
                            .feeToDate(fee.getEndDate());
                }
            });
        } catch (Exception e) {
            logger.atSevere().withCause(e).withStackTrace(StackSize.MEDIUM);
            throw new PatentOfficeException(OfficeError.DOCUMENT_PARSE, "Не удалось разобрать документ.");
        }

        logger.atInfo().log("Patent was parsed for %d ms", System.currentTimeMillis() - start);

        return patent.build();
    }

    /**
     * Finds patent application number.
     * <p>
     * In this:
     * "(21) Регистрационный номер заявки | 200701512"
     * Html code looks like this:
     * <tr><td class=c width=50%>(21) Регистрационный номер заявки</td><td class=c width=50%>200701512</td></tr>
     *
     * @return Patent application number.
     * @see Inid#APPLICATION_NUMBER
     */
    private String findPatentApplicationNumber() {
        final Matcher matcher = getMatcherI(">\\(" + Inid.APPLICATION_NUMBER + "\\).*?</td><td.*?>(\\d*)");

        return logInid(Inid.APPLICATION_NUMBER, matcher.find() ? matcher.group(1) : "");
    }

    /**
     * Finds patent application date.
     * <p>
     * In this:
     * "(22) Дата подачи заявки	| 2006.01.12"
     * Html code looks like this:
     * <tr><td class=c width=50%>(22) Дата подачи заявки</td><td class=c width=50%>2006.01.12</td></tr>
     *
     * @return Patent application date.
     * @see Inid#APPLICATION_DATE
     */
    private LocalDate findPatentApplicationDate() {
        final var matcher = getMatcherI(">\\(" + Inid.APPLICATION_DATE + "\\).*?</td><td.*?>(\\d{4}.\\d{2}.\\d{2})");
        final var date = matcher.find() ? toDateFormat(matcher.group(1)) : null;
        logger.atFine().log("%s -> %s", Inid.APPLICATION_DATE, date);

        return date;
    }

    /**
     * Finds patent start date.
     * <p>
     * Equals publication date?
     */
    private LocalDate findPatentStartDate() {
        return findPatentPubDate();
    }

    /**
     * Finds patent publication date.
     * <p>
     * In this:
     * "(45) (13) Дата публикации патента, код вида документа	B1 2009.10.30 Бюллетень №5"
     * Html code looks like this:
     * <tr><td class=c width=50%>(45) (13) Дата публикации патента, код вида документа</td>
     * <td class=c width=50%>B1 2009.10.30 Бюллетень №5<BR></td></tr><tr>
     *
     * @return Patent publication date.
     * @see Inid#PUBLICATION_DATE
     */
    private LocalDate findPatentPubDate() {
        final Matcher matcher = EapoParsingPattern.PROPERTY_PUBLICATION_DATE.matcher(text);
        final var date = matcher.find() ? toDateFormat(matcher.group(1)) : null;
        logger.atFine().log("%s -> %s", Inid.PUBLICATION_DATE, date);

        return date;
    }

    /**
     * Finds patent number.
     * <p>
     * In this:
     * "(11) Номер патента | 012345"
     * Html code looks like this:
     * <tr><td class=c width=50%>(11) Номер патента</td><td class=c width=50%>012345</td></tr>
     *
     * @return Patent number.
     * @see Inid#PATENT_NUMBER
     */
    private String findPatentNumber() {
        final Matcher matcher = EapoParsingPattern.PROPERTY_NUMBER.matcher(text);

        return logInid(Inid.PATENT_NUMBER, matcher.find() ? matcher.group(1) : "");
    }

    /**
     * Finds patent reg. country code.
     * <p>
     * In this:
     * "(33) Код страны, идентифицирующий ... | US"
     * Html code looks like this:
     * <tr><td class=c width=50%>(33) Код страны, идентифицирующий ...</td><td class=c width=50%>US</td></tr><tr>
     *
     * @return Patent country code.
     * @see Inid#NATIONAL_OFFICE
     */
    private String findPatentCountry() {
        final Matcher matcher = getMatcherI(">\\(" + Inid.NATIONAL_OFFICE + "\\).*?</td><td.*?>([a-zA-Z]{2})");

        return logInid(Inid.NATIONAL_OFFICE, matcher.find() ? matcher.group(1) : "");
    }

    /**
     * Finds all the patent proprietors.
     * <p>
     * In this:
     * "(73) Сведения о патентовладельцах | САЙМА ЛЭБС ИНК. (US)"
     * Html code looks like this:
     * <tr><td class=c width=50%>(73) Сведения о патентовладельцах</td>
     * <td class=c width=50%>САЙМА ЛЭБС ИНК.   (US)</td></tr>
     *
     * @return Proprietors list.
     * @see Inid#OWNERS_NAMES
     */
    private List<String> findPatentProprietors() {
        final Matcher matcher = getMatcherI(">\\(" + Inid.OWNERS_NAMES + "\\).*?</td><td.*?>(.*?)<");

        List<String> result = matcher.find() ? processProprietors(matcher.group(1)) : new ArrayList<>();

        logInid(Inid.OWNERS_NAMES, result.toString());

        return result;
    }

    private List<String> processProprietors(String text) {
        return Arrays.stream(
                // Explode by comma ,
                text.split(Pattern.quote(", ")))
                // remove languages
                .map(name -> name.replaceAll("\\([A-Z]{2}\\)?", ""))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * Finds patent status.
     * <p>
     * Statuses:
     * 1. Патент прекратил свое действие во всех государствах
     * 2. +
     */
    private String findPatentStatus() {
        return logValue("status",
                text.contains("Патент прекратил свое действие во всех государствах") ? "terminated" : "active");
    }

    /**
     * Finds patent correction data if present.
     */
    private String findCorrectionsText() {
        findCorrections();

        return cache.get("corrections");
    }

    private boolean findCorrections() {
        boolean hasCorrections = false;

        if (!cache.getOrDefault("corrections", "").isEmpty()) {
            hasCorrections = true;
        } else {
            final Matcher matcher = Pattern.compile(
                    "Сведения об изменении правового статуса.*[\\s ]*<tr><td.*[\\s ]*(<table(?:.|\\s)*?</table>)")
                    .matcher(text);
            if (matcher.find()) {
                cache.put("corrections", matcher.group(1));
                hasCorrections = true;
            } else {
                cache.put("corrections", "");
            }
        }

        logValue("corrections", hasCorrections ? "+" : "-");

        return hasCorrections;
    }

    /**
     * Finds property's payment data.
     *
     * @return The optional fee payment data result.
     */
    private Optional<PatentFeeDto> findPatentFee() {
        Optional<PatentFeeDto> fee = Optional.empty();

        final Matcher m = EapoParsingPattern.PAYMENTS.matcher(text);

        final List<List<String>> paymentRecords = new ArrayList<>();
        while (m.find()) {
            List<String> paymentRecord = new ArrayList<>();
            for (int gr = 1; gr <= m.groupCount(); gr++) {
                paymentRecord.add(m.group(gr));
            }

            if (paymentRecord.size() > 0) {
                paymentRecords.add(paymentRecord);
            }
        }

        if (paymentRecords.size() > 2) {
            List<String> latestRussianPayment = new ArrayList<>();
            for (List<String> record : paymentRecords) {
                try {
                    final String checkValue = record.get(7).toLowerCase();
                    if (checkValue.contains("x")) {
                        latestRussianPayment = record;
                    }
                } catch (IndexOutOfBoundsException e) {
                    latestRussianPayment = new ArrayList<>();
                }
            }

            final List<String> paidCounties = new ArrayList<>();
            if (!latestRussianPayment.isEmpty()) {
                paidCounties.add("RU");
                fee = Optional.of(
                        new PatentFeeDto("",
                                latestRussianPayment.get(0),
                                toDateFormat(parsePayDates(latestRussianPayment.get(1), 1)),
                                toDateFormat(parsePayDates(latestRussianPayment.get(1), 2)),
                                paidCounties));
            }
        }

        return logChunk("fee", fee);
    }

    // Parses payment dates like so "С 2013.07.18 по 2014.07.17"
    private String parsePayDates(String text, int index) {
        final Matcher m = Pattern.compile(".*? (\\d{4}.\\d{2}.\\d{2}) .*? (\\d{4}.\\d{2}.\\d{2})",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS).matcher(text);

        return m.find() ? m.group(index) : "";
    }

    public static String getUrl(String... params) {
        return "https://www.eapo.org/ru/patents/reestr/patent.php?id=" + params[0];
    }

    @Override
    protected void setDocumentDateFormat() {
        documentDateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    }
}
