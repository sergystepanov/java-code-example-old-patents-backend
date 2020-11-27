package ru.ineureka.patents.dto.patent;

import java.util.List;

public class PatentFeeDto {
    private final String message;
    private final String annuity;
    private final String startDate;
    private final String endDate;
    private final List<String> countries;

    public PatentFeeDto(String message, String annuity, String startDate, String endDate, List<String> countries) {
        this.message = message;
        this.annuity = annuity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countries = countries;
    }

    public String getMessage() {
        return message;
    }

    public String getAnnuity() {
        return annuity;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public List<String> getCountries() {
        return countries;
    }

    @Override
    public String toString() {
        return "PatentFeeDto {message=" + message + ", annuity=" + annuity + ", startDate=" + startDate +
                ", endDate=" + endDate + ", countries=" + countries + '}';
    }
}
