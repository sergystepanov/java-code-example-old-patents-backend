package ru.ineureka.patents.legacy.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PaymentCaseDto {
    private final String id;
    private final String count;
    private final String dateCreated;
    private final String doc;
    private final String maxDueDate;
    private final String minDueDate;
    private final String partnerId;

    @JsonCreator
    public PaymentCaseDto(@JsonProperty("id") String id,
                          @JsonProperty("count") String count,
                          @JsonProperty("date_created") String dateCreated,
                          @JsonProperty("doc") String doc,
                          @JsonProperty("max_due_date") String maxDueDate,
                          @JsonProperty("min_due_date") String minDueDate,
                          @JsonProperty("partner_id") String partnerId) {
        this.id = id;
        this.count = count;
        this.dateCreated = dateCreated;
        this.doc = doc;
        this.maxDueDate = maxDueDate;
        this.minDueDate = minDueDate;
        this.partnerId = partnerId;
    }

    public String getId() {
        return id;
    }

    public String getCount() {
        return count;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDoc() {
        return doc;
    }

    public String getMaxDueDate() {
        return maxDueDate;
    }

    public String getMinDueDate() {
        return minDueDate;
    }

    public String getPartnerId() {
        return partnerId;
    }

    @Override
    public String toString() {
        return "PaymentCaseDto{" +
                "id='" + id + '\'' +
                ", count='" + count + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", doc='" + doc + '\'' +
                ", maxDueDate='" + maxDueDate + '\'' +
                ", minDueDate='" + minDueDate + '\'' +
                ", partnerId='" + partnerId + '\'' +
                '}';
    }
}
