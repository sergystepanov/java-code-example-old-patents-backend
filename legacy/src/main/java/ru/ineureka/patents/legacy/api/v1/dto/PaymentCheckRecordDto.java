package ru.ineureka.patents.legacy.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PaymentCheckRecordDto {
    private final String id;
    private final String annuity;
    private final String annuityEnd;
    private final String applicationNo;
    private final String doc;
    private final String dueDate;
    private String paid;
    private final String registrationNumber;
    private final String registry;
    private final String type;
    private boolean cached;
    private boolean pdf;

    @JsonCreator
    public PaymentCheckRecordDto(@JsonProperty("id") String id,
                                 @JsonProperty("annuity") String annuity,
                                 @JsonProperty("annuity_end") String annuityEnd,
                                 @JsonProperty("application_no") String applicationNo,
                                 @JsonProperty("doc") String doc,
                                 @JsonProperty("due_date") String dueDate,
                                 @JsonProperty("paid") String paid,
                                 @JsonProperty("registration_number") String registrationNumber,
                                 @JsonProperty("registry") String registry,
                                 @JsonProperty("type") String type) {
        this.id = id;
        this.annuity = annuity;
        this.annuityEnd = annuityEnd;
        this.applicationNo = applicationNo;
        this.doc = doc;
        this.dueDate = dueDate;
        this.paid = paid;
        this.registrationNumber = registrationNumber;
        this.registry = registry;
        this.type = type;
        this.cached = false;
        this.pdf = false;
    }

    public String getId() {
        return id;
    }

    public String getAnnuity() {
        return annuity;
    }

    public String getAnnuityEnd() {
        return annuityEnd;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public String getDoc() {
        return doc;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPaid() {
        return paid;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getRegistry() {
        return registry;
    }

    public String getType() {
        return type;
    }

    public boolean isCached() {
        return cached;
    }

    public boolean isPdf() {
        return pdf;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    @Override
    public String toString() {
        return "PaymentCheckRecordDto{" +
                "id='" + id + '\'' +
                ", annuity='" + annuity + '\'' +
                ", annuityEnd='" + annuityEnd + '\'' +
                ", applicationNo='" + applicationNo + '\'' +
                ", doc='" + doc + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", paid='" + paid + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", registry='" + registry + '\'' +
                ", type='" + type + '\'' +
                ", cached=" + cached +
                ", pdf=" + pdf +
                '}';
    }
}
