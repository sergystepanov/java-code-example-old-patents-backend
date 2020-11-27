package ru.ineureka.patents.office.dto;

import java.time.LocalDate;

public class PropertyApplication {
    private final String number;
    private final LocalDate date;
    private final String link;

    public PropertyApplication(String number, LocalDate date, String link) {
        this.number = number;
        this.date = date;
        this.link = link;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNumber() {
        return number;
    }

    public String getLink() {
        return link;
    }
}
