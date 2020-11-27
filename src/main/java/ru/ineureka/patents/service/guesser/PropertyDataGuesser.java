package ru.ineureka.patents.service.guesser;

import com.google.common.flogger.FluentLogger;
import ru.ineureka.patents.dto.type.PropertyTypeName;
import ru.ineureka.patents.service.cases.Fields;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import static ru.ineureka.patents.office.Office.EAPO;
import static ru.ineureka.patents.office.Office.FIPS;
import static ru.ineureka.patents.service.cases.Fields.*;

/**
 * The object which guesses some data of a property.
 *
 * @since 3.0.0
 */
public final class PropertyDataGuesser {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private static final GuesserDateFormat[] formats = new GuesserDateFormat[]{
            new GuesserDateFormat("yyyy-MM-dd"),
            new GuesserDateFormat("MM-dd-yy"),
            new GuesserDateFormat("dd.MM.yyyy"),
            new GuesserDateFormat("dd MMM yyyy", Locale.ENGLISH),
            new GuesserDateFormat("d['st']['nd']['rd']['th'] MMM yyyy", Locale.ENGLISH),
            new GuesserDateFormat("dd-MMM-yy", Locale.ENGLISH),
            new GuesserDateFormat("M/d/yy")
    };
    private static final DateTimeFormatter datePattern = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Pattern numericOnly = Pattern.compile("[^\\d]");

    /**
     * Guesses a date and returns it it as YMD-formatted string.
     * <p>
     * Example:
     * <pre>
     * PropertyDataGuesser guesser = new PropertyDataGuesser();
     * var date = guesser.getDate("1 Jan 2010");
     * // date == "2010-01-01"
     * </pre>
     *
     * @param date A date to convert.
     * @return A formatted date.
     * @throws NullPointerException In case of null date.
     * @since 3.0.0
     */
    public String getDate(String date) {
        for (var format : formats) {
            try {
                return LocalDate.parse(date, format.isLocalized() ?
                        DateTimeFormatter.ofPattern(format.getFormat(), format.getLocale()) :
                        DateTimeFormatter.ofPattern(format.getFormat()))
                        .format(datePattern);
            } catch (DateTimeParseException e) {
                logger.atFine().log("Failed to format %s with the %s", date, format);
            }
        }

        return date;
    }

    /**
     * Guesses the registry of the property.
     *
     * @param data The input data fields.
     * @return One of the registries.
     * @since 3.0.0
     */
    public String getRegistry(Map<String, String> data) {
        final String nation = value(data, NATION).toLowerCase();
        final String description = value(data, DESCRIPTION).toLowerCase();

        return (nation.equals("eurasia (ea)") || nation.equals("eurasian") || description.equals("eurasian patent")) ?
                EAPO : FIPS;
    }

    /**
     * Guesses the property's payment annuity period.
     *
     * @param data The input data fields.
     * @return A period value as array of two values (start, end).
     * @since 3.0.0
     */
    public String[] getAnnuityPeriod(Map<String, String> data) {
        String[] annuities = new String[2];
        final String type = value(data, TYPE);
        final String applicationDate = value(data, APPLICATION_DATE);
        final String annuity = value(data, PAYMENT_ANNUITY);

        annuities[0] = annuity;
        annuities[1] = annuity;

        // Calculations for the D property type
        final boolean isDesignProperty = PropertyTypeName.D.is(type);
        // Every property of type D could be paid for multiple years if its application date is after the year of 2015
        final boolean isAfterY2015 = applicationDate.compareTo("2015") >= 0;
        final boolean hasStartAnnuity = !annuity.isEmpty();
        if (isDesignProperty && isAfterY2015 && hasStartAnnuity) {
            // Try to parse annuity
            // Start value
            // Guess if it contains 0, 1 separate values
            if (annuity.contains(",")) {
                final String[] result = annuity.split(",");
                if (result.length > 0) {
                    annuities[0] = result[0].trim();
                }
            } else {
                // Guess if contains 0-5 range values
                if (annuity.contains("-")) {
                    final String[] result = annuity.split("-");
                    if (result.length > 0) {
                        annuities[0] = result[0].trim();
                    }
                }
            }

            // Calculates the closest upper border of a payment year multiple of 5
            var startYear = Integer.parseInt(removeNotNumeric(annuities[0]));
            if (startYear % 5 != 0) {
                annuities[1] = String.valueOf(startYear + 5 - (startYear % 5));
            }
        }

        annuities[0] = removeNotNumeric(annuities[0]);
        annuities[1] = removeNotNumeric(annuities[1]);

        return annuities;
    }

    /**
     * Guesses the property's grace period of payment.
     * For every property it just adds a 6 month value.
     *
     * @param data The input data fields.
     * @return A end period value.
     * @since 3.0.0
     */
    public String getGracePeriod(Map<String, String> data) {
        final String dueDate = value(data, DUE_DATE);

        if (!dueDate.isBlank()) {
            try {
                return LocalDate.parse(dueDate, datePattern)
                        .plusMonths(6)
                        .format(datePattern);
            } catch (DateTimeParseException e) {
                logger.atFine().log("Failed to format %s with the default date format", dueDate);
            }
        }

        return "";
    }

    /**
     * Guesses and returns an YMD-formatted string of property's expiry date.
     *
     * @param data        The input data.
     * @param yearsActive The value of property's active years.
     * @since 3.0.0
     */
    public String getExpiryDate(Map<String, String> data, long yearsActive) {
        final String applicationDate = value(data, APPLICATION_DATE);

        if (!applicationDate.isBlank()) {
            try {
                return LocalDate.parse(applicationDate, datePattern)
                        .plusYears(yearsActive)
                        .format(datePattern);
            } catch (DateTimeParseException e) {
                logger.atFine().log("Failed to format %s with the default date format", applicationDate);
            }
        }

        return "";
    }

    private static String removeNotNumeric(String value) {
        return numericOnly.matcher(value).replaceAll("");
    }

    private static String value(Map<String, String> data, Fields field) {
        return data.getOrDefault(field.toString(), "");
    }
}
