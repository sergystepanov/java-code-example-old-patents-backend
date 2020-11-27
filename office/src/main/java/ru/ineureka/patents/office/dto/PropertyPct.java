package ru.ineureka.patents.office.dto;

import java.time.LocalDate;

public class PropertyPct {
    private final String number;
    private final LocalDate date;

    public PropertyPct(String number, LocalDate date) {
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
