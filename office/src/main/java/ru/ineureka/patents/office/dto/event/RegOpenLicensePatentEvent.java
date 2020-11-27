package ru.ineureka.patents.office.dto.event;

import java.time.LocalDate;

public final class RegOpenLicensePatentEvent extends PatentLegalEvent {
    public static final String code = "QA4A/QA1K";
    private final boolean isOpenLicense;
    private final LocalDate registrationDate;

    public RegOpenLicensePatentEvent(boolean isOpenLicense, LocalDate registrationDate) {
        this.isOpenLicense = isOpenLicense;
        this.registrationDate = registrationDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public boolean isOpenLicense() {
        return isOpenLicense;
    }
}
