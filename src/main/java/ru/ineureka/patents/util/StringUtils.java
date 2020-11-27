package ru.ineureka.patents.util;

import java.util.regex.Pattern;

public final class StringUtils {
    public static Pattern notNumeric = Pattern.compile("[^\\d.]");

    private StringUtils() {
    }
}
