package ru.ineureka.patents.office.dto;

import java.time.LocalDate;

public final class PatentApplicationDto {
    private final String number;
    private final LocalDate date;

    public PatentApplicationDto(String number, LocalDate date) {
        this.number = number;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }
}
