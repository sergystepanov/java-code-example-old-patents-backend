package ru.ineureka.patents.dto.client;

public class CaseMessageDto {
    private final long id;
    private final long for_id;
    private final String for_field;
    private final String type;
    private final String message;

    public CaseMessageDto(long id, long for_id, String for_field, String type, String message) {
        this.id = id;
        this.for_id = for_id;
        this.for_field = for_field;
        this.type = type;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public long getFor_id() {
        return for_id;
    }

    public String getFor_field() {
        return for_field;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
