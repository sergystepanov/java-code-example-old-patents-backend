package ru.ineureka.patents.persistence.property;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "PROPERTY")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ex grant_no
    @Column(name = "registration_number")
    private String registrationNumber;

    private Long type;

    @Column(name = "patent_office")
    private Long patentOffice;

    private String nation;

    @Column(name = "application_no")
    private String applicationNumber;

    @Column(name = "application_no_ex")
    private String getApplicationNumberExtended;

    @Column(name = "application_date")
    private Date applicationDate;

    @Column(name = "grant_date")
    private Date grantDate;

    private String description;

    private String status;

    @Column(name = "open_license")
    private Boolean isOpenLicense;

    protected Property() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getPatentOffice() {
        return patentOffice;
    }

    public void setPatentOffice(Long patentOffice) {
        this.patentOffice = patentOffice;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getGetApplicationNumberExtended() {
        return getApplicationNumberExtended;
    }

    public void setGetApplicationNumberExtended(String getApplicationNumberExtended) {
        this.getApplicationNumberExtended = getApplicationNumberExtended;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(Date grantDate) {
        this.grantDate = grantDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getOpenLicense() {
        return isOpenLicense;
    }

    public void setOpenLicense(Boolean openLicense) {
        isOpenLicense = openLicense;
    }
}
