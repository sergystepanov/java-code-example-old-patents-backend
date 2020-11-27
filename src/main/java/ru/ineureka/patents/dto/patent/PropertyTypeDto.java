package ru.ineureka.patents.dto.patent;

public final class PropertyTypeDto {
    private final long id;
    private final String code;
    private final int duration_years;
    private final String name;
    private final String description;

    public PropertyTypeDto(long id, String code, int duration_years, String name, String description) {
        this.id = id;
        this.code = code;
        this.duration_years = duration_years;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getDuration_years() {
        return duration_years;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
