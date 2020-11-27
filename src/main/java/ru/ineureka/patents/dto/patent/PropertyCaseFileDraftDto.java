package ru.ineureka.patents.dto.patent;

public final class PropertyCaseFileDraftDto {
    private final long id;
    private final String name;
    private final String checksum;
    private final String text;
    private final String date_created;

    public PropertyCaseFileDraftDto(long id, String name, String checksum, String text, String date_created) {
        this.id = id;
        this.name = name;
        this.checksum = checksum;
        this.text = text;
        this.date_created = date_created;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChecksum() {
        return checksum;
    }

    public String getText() {
        return text;
    }

    public String getDate_created() {
        return date_created;
    }
}
