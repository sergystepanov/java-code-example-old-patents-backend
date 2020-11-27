package ru.ineureka.patents.legacy.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PaymentCaseRecordRequestDto {
    private final String number;
    private final String office;
    private final String type;
    private final String years;

    @JsonCreator
    public PaymentCaseRecordRequestDto(@JsonProperty("number") String number,
                                       @JsonProperty("office") String office,
                                       @JsonProperty("type") String type,
                                       @JsonProperty("years") String years) {
        this.number = number;
        this.office = office;
        this.type = type;
        this.years = years;
    }

    public String getNumber() {
        return number;
    }

    public String getOffice() {
        return office;
    }

    public String getType() {
        return type;
    }

    public String getYears() {
        return years;
    }
}
