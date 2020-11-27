package ru.ineureka.patents.office.org.eapo;

import com.google.common.flogger.FluentLogger;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Eurasian patent organisation (EAPO) patent number search.
 *
 * @since 3.0.0
 */
public final class EapoPublicationNumberSearch {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    // The main searching pattern for the number
    private static final Pattern NUMBER = Pattern.compile("patent\\.php\\?id=(\\d+)");

    public static String getUrl(String number) {
        return "https://www.eapo.org/ru/patents/reestr/search.php?SEARCH[i13]=B&SEARCH[i21]=" + number;
    }

    public static Optional<String> getNumber(String text) {
        String number = "";
        final Matcher matcher = NUMBER.matcher(text);

        if (matcher.find()) {
            number = matcher.group(1);
            logger.atSevere().log("Found a property publication number, %s", number);
        }

        return !number.isEmpty() ? Optional.of(number) : Optional.empty();
    }
}
