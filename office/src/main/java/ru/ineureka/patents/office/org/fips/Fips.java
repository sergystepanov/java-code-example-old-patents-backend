package ru.ineureka.patents.office.org.fips;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LazyArgs;
import com.google.common.flogger.StackSize;
import ru.ineureka.patents.office.Office;
import ru.ineureka.patents.office.PatentOffice;
import ru.ineureka.patents.office.dto.*;
import ru.ineureka.patents.office.dto.event.RegOpenLicensePatentEvent;
import ru.ineureka.patents.office.exception.OfficeError;
import ru.ineureka.patents.office.exception.PatentOfficeException;
import ru.ineureka.patents.property.Inid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static ru.ineureka.patents.office.org.fips.FipsParsingPattern.*;

/**
 * The FIPS registry documents reader class.
 * <p>
 * Originally was created on 22.10.13 22:05.
 * </p>
 *
 * @since 1.0.0
 */
public final class Fips extends PatentOffice {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public static final String ENCODING = "windows-1251";
    public static final String URL = "https://www1.fips.ru";

    // various field maps
    private static final Map<String, String> statusMap = Map.of(
            "нет данных", "no_data",
            "не действует", "inactive",
            "действует", "active",
            "может прекратить свое действие", "can_be_terminated",
            "прекратил действие, но может быть восстановлен", "terminated_can_be_recovered",
            "прекратил действие", "terminated"
    );

    // Simple cache
    private final Map<String, Object> cache = new HashMap<>();
    private static final String RAW_PROPRIETORS = "raw_proprietors";

    private final String type;

    public Fips(String type, byte[] bytes) throws PatentOfficeException {
        super(bytes);

        this.type = type;
    }

    /**
     * Checks if the document has all necessary data for processing.
     *
     * @return True if document is ok.
     * @throws PatentOfficeException In case of any exceptions.
     * @since 1.0.0
     */
    @Override
    protected boolean isDocumentAlrightAlrightAlright() throws PatentOfficeException {
        // Hope this something similar for all of these patents
        final Matcher m19 = DOCUMENT_MARKER.matcher(this.text);
        final boolean hasMarker = m19.find();
        logValue("correct document marker (INID 19)", hasMarker ? "+" : "-");

        if (hasMarker) return true;

        // If it has any errors
        final Matcher m = DOCUMENT_ERROR2.matcher(this.text);

        String error = "";
        // Trying to specify what kind of error we have got
        if (m.find()) {
            error = m.group(1);
        } else {
            final Matcher m2 = DOCUMENT_ERROR.matcher(this.text);
            if (m2.find()) {
                error = m2.group(1);
            }
        }

        if (error.isEmpty()) {
            throw new PatentOfficeException(OfficeError.UNDEFINED, error);
        } else {
            logger.atSevere().log("Fips document parse error, %s", error);
            if (error.contains("Превышен допустимый предел количества просмотров документов")) {
                throw new PatentOfficeException(OfficeError.LIMIT_EXCEED, error);
            } else {
                if (error.contains("Слишком быстрый просмотр документов")) {
                    throw new PatentOfficeException(OfficeError.TOO_QUICK, error);
                } else {
                    throw new PatentOfficeException(error);
                }
            }
        }
    }

    @Override
    protected String getEncoding() {
        return "windows-1251";
    }

    @Override
    protected FluentLogger getLogger() {
        return logger;
    }

    public PatentExtendedDto getPatent() throws PatentOfficeException {
        final long start = System.currentTimeMillis();

        if (!isDocumentAlrightAlrightAlright())
            throw new PatentOfficeException(OfficeError.DOCUMENT_PARSE, "Документ не прошёл проверку.");

        final var patent = PatentExtendedDto.builder();

        try {
            final String number = findPatentNumber();
            final var pctApplication = findPatentPctApplication();
            final var pctApplicationDate = pctApplication.map(PropertyPct::getDate).orElse(null);
            final var pctPublication = findPatentPctPublication();
            final var startDate = findPatentStartDate();

            patent
                    .registry(Office.FIPS)
                    .type(this.type)
                    .type_name(getTypeName(this.type))
                    .grant_no(number)
                    .isPublished(!number.isEmpty())
                    .nation(findPatentCountry())
                    .admissionDate(findPatentAdmissionDate())
                    .pctDate(findPatentPCTDate())
                    .pct_application_number(pctApplication.map(PropertyPct::getNumber).orElse(""))
                    .pctApplicationDate(pctApplicationDate)
                    .pct_publication_number(pctPublication.map(PropertyPct::getNumber).orElse(""))
                    .pctPublicationDate(pctPublication.map(PropertyPct::getDate).orElse(null))
                    .grantDate(findPatentPubDate());

            final var start_ = Objects.isNull(startDate) ? pctApplicationDate : startDate;
            patent.startDate(start_);
            cache.put("start_date", start_);

            findPatentStatus().ifPresent(status -> patent
                    .status(status.getName())
                    .actualDate(status.getDate())
                    .status_description(status.getDescription()));
            findPatentFee().ifPresent(fee -> {
                patent
                        .fee_message(fee.getMessage())
                        .fee_year(fee.getAnnuity())
                        .annuity(fee.getAnnuity())
                        .feeFromDate(fee.getStartDate())
                        .feeToDate(fee.getEndDate())
                        .due_date_calculated(fee.getEndDate() == null);
                cache.put("fee_end_date", fee.getEndDate());
            });

            findPatentApplication().ifPresent(application -> {
                List<String> applicationParts = explodeApplication(application.getNumber());
                patent
                        .application_no_full(application.getNumber())
                        .applicationDate(application.getDate())
                        .application_no(applicationParts.size() > 0 ? applicationParts.get(0) : "")
                        .application_no_ex(applicationParts.size() > 1 ? applicationParts.get(1) : "");
                cache.put("application_date", application.getDate());
            });

            findOpenLicense().ifPresent(license -> patent
                    .open_license(license.isOpenLicense())
                    .openLicenseRegDate(license.getRegistrationDate()));

            patent
                    .gracePeriod(
                            calcGracePeriod(
                                    cached("fee_end_date") ? (LocalDate) cache.get("fee_end_date") :
                                            cached("application_date") ? (LocalDate) cache.get("application_date") : null))
                    .proprietors(findPatentProprietors())
                    .corrections(findCorrections())
                    .corrections_text(findCorrectionsText())
                    .domestic_appln_no(findPatentConventions())
                    .expiryDate((LocalDate) cache.get("application_date"))
                    .dueDate((LocalDate) getCached("fee_end_date")
                            .or(() -> getCached("start_date"))
                            .or(() -> getCached("application_date"))
                            .or(() -> getCached("admission_date"))
                            .orElse(null)
                    );

            findParentApplication().ifPresent(app -> patent.parentApplication(
                    new PropertyApplication(app.getNumber(), app.getDate(), app.getLink())
            ));

            patent.isProcessPublication(!((String) cache.getOrDefault("process_publication", "")).isEmpty());
        } catch (Exception e) {
            logger.atSevere().withCause(e).withStackTrace(StackSize.MEDIUM);
            throw new PatentOfficeException(OfficeError.DOCUMENT_PARSE, "Не удалось разобрать документ.");
        }

        logger.atFine().log("Document parse time: %sms", LazyArgs.lazy(() -> System.currentTimeMillis() - start));

        return patent.build();
    }

    /**
     * Returns a patent type name
     *
     * @return mixed
     */
    private String getTypeName(String type) {
        if (type == null) return "";

        final Map<String, String> types = Map.of(
                "", "",
                "p", "изобретение",
                "u", "полезную модель",
                "d", "промышленный образец");

        return types.get(type.toLowerCase());
    }

    /**
     * Finds a properties' number.
     * <p>
     * It extracts text from the property's application first and then from the publication.
     *
     * @return The patent number.
     * @see Inid#PATENT_NUMBER
     */
    private String findPatentNumber() {
        final Matcher firstMatcher = PROPERTY_NUMBER_FROM_APPLICATION.matcher(text);

        String number = "";
        if (firstMatcher.find()) {
            number = firstMatcher.group(1);
            cache.put("process_publication", "1");
        } else {
            final Matcher secondMatcher = PATENT_NUMBER.matcher(text);
            if (secondMatcher.find()) {
                number = secondMatcher.group(1);
            }
        }

        return logInid(Inid.PATENT_NUMBER, number.isEmpty() ? "" : number.replace(" ", ""));
    }

    /**
     * Finds country language code of the patent.
     *
     * @return The language code.
     * @see Inid#PUBLISHING_ORGANIZATION_CODE
     */
    private String findPatentCountry() {
        final Matcher m = PUB_ORG.matcher(this.text);

        return logInid(Inid.PUBLISHING_ORGANIZATION_CODE, m.find() ? m.group(1) : "");
    }

    /**
     * Finds current patent status.
     *
     * @return The patent status.
     */
    private Optional<PatentStatusDto> findPatentStatus() {
        final var m = STATUS.matcher(this.text);

        return logChunk("status", m.find() ?
                Optional.of(new PatentStatusDto(toDateFormat(m.group(2)), m.group(1), parseStatus(m.group(1)))) :
                Optional.empty());
    }

    /**
     * Parsing RU status to its code.
     * <p>
     * Possible statuses:
     * нет данных -> no_data
     * действует -> active
     * может прекратить свое действие -> can_be_terminated
     * прекратил действие, но может быть восстановлен -> terminated_can_be_recovered
     * прекратил действие -> terminated
     * не действует -> inactive
     * unknown in case if nothing found.
     * </p>
     *
     * @param text The status.
     * @return The patent status code.
     */
    private String parseStatus(String text) {
        final String status = text.trim().toLowerCase();

        return statusMap.entrySet().stream()
                .filter((entry) -> status.equalsIgnoreCase(entry.getKey()))
                .findAny()
                .map(Map.Entry::getValue).orElse("unknown");
    }

    /**
     * Finds patent fee.
     * <p>
     * Two options учтена за 3 год с 28.02.2010 по 27.02.2011 and blank or text
     * </p>
     *
     * @return The patent fee.
     */
    private Optional<PatentFeeDto> findPatentFee() {
        Optional<PatentFeeDto> result;

        final var m = FEE.matcher(this.text);
        final var hasData = m.find() && !(m.group(2).isEmpty() || m.group(3).isEmpty() || m.group(4).isEmpty());

        if (hasData) {
            result = Optional.of(new PatentFeeDto(
                    m.group(1), m.group(2), toDateFormat(m.group(3)), toDateFormat(m.group(4)),
                    Collections.singletonList("RU")));
        } else {
            // Try to calculate annuity if not present
            final var startDate = (LocalDate) cache.get("start_date");
            String annuity = "0";

            if (startDate != null) {
                final long days = DAYS.between(startDate, LocalDate.now());
                logger.atFine().log("Days between %s", days);
                if (days > 365) {
                    annuity = "2";
                }
            }

            result = Optional.of(
                    new PatentFeeDto("Рассчитано автоматически", annuity, null, null, Collections.singletonList("RU"))
            );
        }

        logChunk("fee", result);

        return result;
    }

    /**
     * Finds patent's PCT date.
     *
     * @return The PCT date of the patent.
     * @see Inid#PCT_DATE
     */
    private LocalDate findPatentPCTDate() {
        final var m = PCT_DATE.matcher(this.text);
        final var date = m.find() ? toDateFormat(m.group(1)) : null;
        logger.atFine().log("%s -> %s", Inid.PCT_DATE, date);

        return date;
    }

    /**
     * Finds international patent's PCT application.
     * <p>
     * Should be like AA dddd/ddddd dddddddd+.
     * </p>
     *
     * @return The PCT application of the patent.
     * @see Inid#PCT_APPLICATION
     */
    private Optional<PropertyPct> findPatentPctApplication() {
        PropertyPct pct = null;
        final var match = PCT_APP.matcher(this.text);

        while (match.find()) {
            pct = new PropertyPct(match.group(1).trim(), toDateFormat(match.group(2)));
        }
        logger.atFine().log("%s -> %s", Inid.PCT_APPLICATION, pct);

        return Optional.ofNullable(pct);
    }

    /**
     * Finds patent's PCT publication data.
     * <p>
     * Should be like WO dddd/ddddd dddddddd+.
     * </p>
     *
     * @return The PCT publication of the patent.
     * @see Inid#PCT_PUBLICATION_DATA
     */
    private Optional<PropertyPct> findPatentPctPublication() {
        PropertyPct pct = null;
        final var m = PCT_PUB.matcher(this.text);
        while (m.find()) {
            pct = new PropertyPct(m.group(1).trim(), toDateFormat(m.group(2)));
        }
        logger.atFine().log("%s -> %s", Inid.PCT_PUBLICATION_DATA, pct);

        return Optional.ofNullable(pct);
    }

    /**
     * Finds patent's start date.
     *
     * @return The start date.
     * @see Inid#RIGHTS_START_DATE
     */
    private LocalDate findPatentStartDate() {
        final var match = START_DATE.matcher(this.text);
        final var date = match.find() ? toDateFormat(match.group(1)) : null;

        logger.atFine().log("%s -> %s", Inid.RIGHTS_START_DATE, date);

        return date;
    }

    /**
     * Extracts patent publication date.
     *
     * @return The publication date.
     * @see Inid#PUBLICATION_DATE
     */
    private LocalDate findPatentPubDate() {
        final var match = PUB_DATE.matcher(this.text);
        final var date = match.find() ? toDateFormat(match.group(1)) : null;

        logger.atFine().log("%s -> %s", Inid.PUBLICATION_DATE, date);

        return date;
    }

    /**
     * Finds patent application.
     *
     * @return Patent application data.
     * @see Inid#APPLICATION_NUMBER
     * @see Inid#APPLICATION_DATE
     */
    private Optional<PatentApplicationDto> findPatentApplication() {
        String appNum;
        String appDat;

        final Matcher m = APP_DATA.matcher(this.text);
        if (m.find()) {
            appNum = m.group(1);
            appDat = m.group(2);
        } else {
            // In case of DESIGN template
            final Matcher m2 = APP_DATA_D_APP_NUM.matcher(this.text);
            appNum = m2.find() ? m2.group(1) : "";
            final Matcher m3 = APP_DATA_D_APP_DATE.matcher(this.text);
            appDat = m3.find() ? m3.group(1) : null;
        }

        final var applicationDate = toDateFormat(appDat);
        logger.atFine().log("%s -> %s", Inid.APPLICATION_NUMBER, appNum);
        logger.atFine().log("%s -> %s", Inid.APPLICATION_DATE, applicationDate);

        return Optional.of(new PatentApplicationDto(appNum, applicationDate));
    }

    /**
     * If application number in extended format - parse and write to another column.
     *
     * @param applicationNumber An application number in extended format.
     * @return A list with exploded application number parts.
     * @since 1.0.0
     */
    private List<String> explodeApplication(String applicationNumber) {
        return Arrays.asList(applicationNumber.split("/", 2));
    }

    /**
     * Finds additional patent application's admission date.
     *
     * @return date
     */
    private LocalDate findPatentAdmissionDate() {
        final var date = findGenericDate("Дата поступления: <b>(" + DATE_PATTERN + ")<");
        logger.atFine().log("%s -> %s", "Admission date", date);

        return date;
    }

    /**
     * Finds patent proprietors or applicants in patent application case
     *
     * @return array
     */
    private List<String> findPatentProprietors() {
        List<String> result = new ArrayList<>();

        // Cache
        cache.put(RAW_PROPRIETORS, "");

        Matcher main;
        int currentInid = Inid.OWNERS_NAMES;

        // Case #1: Proprietors found
        final Matcher m1 = PROP_CASE1.matcher(this.text);

        // Case #2: Try to find patent applicants as its proprietors
        if (m1.find()) {
            m1.reset();
            main = m1;
        } else {
            final Matcher m2 = PROP_CASE2.matcher(this.text);

            if (m2.find()) {
                m2.reset();
                main = m2;
                currentInid = Inid.APPLICANTS_NAMES;
            } else {
                main = PROP_CASE3.matcher(this.text);
            }
        }

        final Matcher m3 = PROP_CASE3.matcher(this.text);
        if (m3.find()) {
            m3.reset();
            main = m3;
            currentInid = Inid.OWNERS_NAMES;
        }

        String found = "";
        while (main.find()) {
            found = main.group(1);
        }

        if (!found.isEmpty()) {
            // Cache
            cache.put(RAW_PROPRIETORS, found);

            // Get <s>last</s> element and remove all BRs
            String p = found
                    .replace("<BR>", "")
                    .replace("<b>", "")
                    .replace("<B>", "")
                    .replace("<br>", "");

            logger.atFine().log("Proprietors before split: %s", found);

            // Set splitters by (XX),?
            p = PROP_LIST_SPLIT.matcher(p).replaceAll(")|");

            for (String part : p.split(Pattern.quote("|"))) {
                if (!part.isEmpty()) result.add(part.trim());
            }

            logInid(currentInid, result.toString());
        }

        return result;
    }

    /**
     * Finds any correction data in the html.
     *
     * @return True if it is present.
     */
    private boolean findCorrections() {
        return logValue("corrections", CORRECTIONS.matcher(this.text).find() ? "+" : "-").equals("+");
    }

    private String findCorrectionsText() {
        final Matcher m = CORRECTIONS_TEXT.matcher(this.text);

        return logValue("corrections' text", m.find() ? m.group(1) : "");
    }

    private Optional<RegOpenLicensePatentEvent> findOpenLicense() {
        final boolean hasOpenLicense = OL.matcher(this.text).find();
        logValue("open license", hasOpenLicense ? "+" : "-");

        LocalDate date = null;
        final Matcher m = OL_D.matcher(this.text);
        if (m.find()) {
            date = toDateFormat(m.group(1));
            logger.atFine().log("%s -> %s", "Open license", date);
        }

        return hasOpenLicense ? Optional.of(new RegOpenLicensePatentEvent(true, date)) : Optional.empty();
    }

    private List<String> findPatentConventions() {
        List<String> result = new ArrayList<>();

        final Matcher m = CONVENT.matcher(this.text);

        // Should be like:
        // 28.05.2009 KR 10-2009-0047192<BR> 27.08.2009 KR 10-2009-0079950<BR> 24.05.2010 KR 10-2010-0047877<BR>
        while (m.find()) {
            String found = m.group(1);

            // Slice by some HTML tags
            result = Pattern.compile("(<br?>)", Pattern.CASE_INSENSITIVE)
                    .splitAsStream(found)
                    .map(String::trim)
                    .map(s -> s.replaceAll("\\s+", " "))
                    .filter(f -> !f.isEmpty())
                    .collect(Collectors.toList());
        }

        logInid(Inid.PRIORITIES, result.toString());

        return result;
    }

    private Optional<PropertyApplication> findParentApplication() {
        PropertyApplication application = null;
        final Matcher m = PARENT_APP_DATE_NUM.matcher(this.text);
        if (m.find()) {
            application = new PropertyApplication(
                    m.group(2),
                    toDateFormat(m.group(3)),
                    toAbsoluteLink(m.group(1))
            );
        }
        logger.atFine().log("%s -> %s", Inid.PARENT_APP, application);

        return Optional.ofNullable(application);
    }

    private LocalDate calcGracePeriod(LocalDate date) {
        if (Objects.isNull(date)) return null;

        final var endDate = date.plusMonths(6);
        logger.atFine().log("%s -> %s", "Grace period", endDate);

        return endDate;
    }

    /**
     * Makes raw html input as beautiful as dragonfly
     *
     * @return string
     */
    public String tidyHtml() {
        String result = this.text;

        // Вырезаем ошибки БД
        result = Pattern.compile("</html>.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
                .matcher(result)
                .replaceAll("</html>");

        return result;
    }

    public static String getUrl(String propertyType, String propertyNumber) {
        Objects.requireNonNull(propertyType);
        Objects.requireNonNull(propertyNumber);

        return URL + "/fips_servl/fips_servlet?DB=" + getFipsType(propertyType) + "&DocNumber=" + propertyNumber;
    }

    /**
     * Casting patent types for patent requests.
     *
     * @param type A type to cast.
     * @return The type cast.
     */
    private static String getFipsType(String type) {
        String result;

        switch (type.toLowerCase()) {
            case "u":
                result = "RUPM";
                break;
            case "d":
                result = "RUDE";
                break;
            case "p":
            default:
                result = "RUPAT";
        }

        return result;
    }

    private LocalDate findGenericDate(String pattern, int flags) {
        LocalDate date = null;
        final var m = Pattern.compile(pattern, flags).matcher(text);
        // TODO only last
        while (m.find()) {
            date = toDateFormat(m.group(1));
        }

        return date;
    }

    @Override
    protected void setDocumentDateFormat() {
        documentDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    private LocalDate findGenericDate(String pattern) {
        return findGenericDate(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    private boolean cached(String key) {
        final var notNull = cache.containsKey(key) && !Objects.isNull(cache.get(key));

        var notEmpty = true;
        if (notNull) {
            final var value = cache.get(key);
            if (value instanceof String) {
                notEmpty = !((String) value).isEmpty();
            }
        }

        return notNull && notEmpty;
    }

    private Optional<Object> getCached(String key) {
        return cached(key) ? Optional.of(cache.get(key)) : Optional.empty();
    }

    private String toAbsoluteLink(String link) {
        if (link == null || link.isBlank()) return link;

        if (link.startsWith("/")) {
            link = URL + link;
        }

        return link.replace("&amp;", "&");
    }
}
