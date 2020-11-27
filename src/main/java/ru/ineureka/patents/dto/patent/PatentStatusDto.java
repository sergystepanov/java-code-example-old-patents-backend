package ru.ineureka.patents.dto.patent;

public final class PatentStatusDto {

    private final String date;
    private final String description;
    private final String name;

    public PatentStatusDto(String date, String description, String name) {
        this.date = date;
        this.description = description;
        this.name = name;
    }

    public String getDate() {
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
