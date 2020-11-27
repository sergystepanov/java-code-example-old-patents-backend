package ru.ineureka.patents.office.dto;

import java.time.LocalDate;

public final class PatentStatusDto {
    private final LocalDate date;
    private final String description;
    private final String name;

    public PatentStatusDto(LocalDate date, String description, String name) {
        this.date = date;
        this.description = description;
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PatentStatusDto {date=" + date + ", name=" + name + ", description=" + description + '}';
    }
}
