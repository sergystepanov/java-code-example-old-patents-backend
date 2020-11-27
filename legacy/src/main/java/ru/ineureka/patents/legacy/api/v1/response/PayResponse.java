package ru.ineureka.patents.legacy.api.v1.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PayResponse {
    private final int paid;

    @JsonCreator
    public PayResponse(@JsonProperty("paid") int paid) {
        this.paid = paid;
    }

    public int getPaid() {
        return paid;
    }

    @Override
    public String toString() {
        return "PayResponse{" +
                "paid=" + paid +
                '}';
    }
}
