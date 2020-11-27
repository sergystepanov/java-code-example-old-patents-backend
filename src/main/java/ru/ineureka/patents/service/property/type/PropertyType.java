package ru.ineureka.patents.service.property.type;

public enum PropertyType {
    PATENT("p"), UTILITY("u"), DESIGN("d");

    private final String oneLetterAbbr;

    PropertyType(String oneLetterAbbr) {
        this.oneLetterAbbr = oneLetterAbbr;
    }

    public String getAbbr() {
        return oneLetterAbbr;
    }

    public static PropertyType fromAbbr(String abbr) {
        return PropertyType.valueOf(abbr.toLowerCase());
    }
}
