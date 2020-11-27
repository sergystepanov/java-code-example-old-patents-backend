package ru.ineureka.patents.service.property;

import java.util.Objects;
import java.util.regex.Pattern;

public final class PropertyRequest {

    private static final Pattern NOT_NUMERIC = Pattern.compile("[^\\d.]");
    private final String office;
    private final String type;
    private final String number;
    private final String years;

    private final String fileName;

    public PropertyRequest(String office, String type, String number) {
        this(office, type, number, "");
    }

    public PropertyRequest(String office, String type, String number, String years) {
        this.office = office;
        this.type = type != null ? type.toLowerCase() : "p";
        this.number = NOT_NUMERIC.matcher(number).replaceAll("");
        this.years = years;

        fileName = getOffice() + "_" + getType() + "_" + getNumber() + (!years.isEmpty() ? "_" + years : "");
    }

    public String getOffice() {
        return office;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public String getYears() {
        return years;
    }

    /**
     * Returns a generated filename for a cache file.
     *
     * @since 3.0.0
     */
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyRequest that = (PropertyRequest) o;
        return Objects.equals(office, that.office) &&
                Objects.equals(type, that.type) &&
                Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(office, type, number);
    }

    @Override
    public String toString() {
        return "PropertyRequest{" +
                "office='" + office + '\'' +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
