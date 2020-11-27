package ru.ineureka.patents.office.dto;

import java.time.LocalDate;
import java.util.List;

public class PatentFeeDto {
    private final String message;
    private final String annuity;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<String> countries;

    public PatentFeeDto(String message, String annuity, LocalDate startDate, LocalDate endDate, List<String> countries) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
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
