package ru.ineureka.patents.dto.patent;

public final class PropertyOfficeDto {
    private final long id;
    private final String code;

    public PropertyOfficeDto(long id, String code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
