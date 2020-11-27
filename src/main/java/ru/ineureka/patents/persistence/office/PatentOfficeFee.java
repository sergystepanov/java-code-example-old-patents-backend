package ru.ineureka.patents.persistence.office;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;

@Entity(name = "PATENT_OFFICE_FEE")
public class PatentOfficeFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long propertyType;

    private Long patentOffice;

    private Integer year;

    private Date since;

    private BigDecimal cost;

    private String country;

    private String currency;

    private String part;

    protected PatentOfficeFee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Long propertyType) {
        this.propertyType = propertyType;
    }

    public Long getPatentOffice() {
        return patentOffice;
    }

    public void setPatentOffice(Long patentOffice) {
        this.patentOffice = patentOffice;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
