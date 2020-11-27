package ru.ineureka.patents.dto.patent;

public final class PatentApplicationDto {
    private final String number;
    private final String date;

    public PatentApplicationDto(String number, String date) {
        this.number = number;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }
}
