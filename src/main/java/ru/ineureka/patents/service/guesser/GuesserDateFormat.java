package ru.ineureka.patents.service.guesser;

import java.util.Locale;

public final class GuesserDateFormat {
    private final String format;
    private final Locale locale;

    GuesserDateFormat(String format, Locale locale) {
        this.format = format;
        this.locale = locale;
    }

    GuesserDateFormat(String format) {
        this.format = format;
        this.locale = null;
    }

    boolean isLocalized() {
        return this.locale != null;
    }

    String getFormat() {
        return this.format;
    }

    Locale getLocale() {
        return this.locale;
    }

    public String toString() {
        return "GuesserDateFormat(format=" + this.getFormat() + ", locale=" + this.getLocale() + ")";
    }
}
