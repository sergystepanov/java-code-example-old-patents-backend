package ru.ineureka.patents.dto.patent;

public class PatentOfficeFeeDto {

    private final long id;
    private final long property_type;
    private final long patent_office;
    private final int year;
    private final double cost;
    private final String country;
    private final String currency;
    private final String part;

    public PatentOfficeFeeDto(long id, long property_type, long patent_office, int year, double cost, String country, String currency, String part) {
        this.id = id;
        this.property_type = property_type;
        this.patent_office = patent_office;
        this.year = year;
        this.cost = cost;
        this.country = country;
        this.currency = currency;
        this.part = part;
    }

    public long getId() {
        return this.id;
    }

    public long getProperty_type() {
        return this.property_type;
    }

    public long getPatent_office() {
        return this.patent_office;
    }

    public int getYear() {
        return this.year;
    }

    public double getCost() {
        return this.cost;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getPart() {
        return this.part;
    }

    public String toString() {
        return "PatentOfficeFeeDto(id=" + this.getId() + ", property_type=" + this.getProperty_type() + ", patent_office=" + this.getPatent_office() + ", year=" + this.getYear() + ", cost=" + this.getCost() + ", country=" + this.getCountry() + ", currency=" + this.getCurrency() + ", part=" + this.getPart() + ")";
    }
}
