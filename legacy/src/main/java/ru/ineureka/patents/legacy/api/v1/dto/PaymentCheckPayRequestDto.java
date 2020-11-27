package ru.ineureka.patents.legacy.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class PaymentCheckPayRequestDto {
    private final String[] ids;

    @JsonCreator
    public PaymentCheckPayRequestDto(@JsonProperty("ids") String[] ids) {
        this.ids = ids;
    }

    public String[] getIds() {
        return ids;
    }
}
