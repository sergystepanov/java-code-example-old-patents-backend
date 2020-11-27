package ru.ineureka.patents.dto.patent.event;

public final class RegOpenLicensePatentEvent extends PatentLegalEvent {

    public static final String code = "QA4A/QA1K";
    private final boolean isOpenLicense;
    private final String registrationDate;

    public RegOpenLicensePatentEvent(boolean isOpenLicense, String registrationDate) {
        this.isOpenLicense = isOpenLicense;
        this.registrationDate = registrationDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public boolean isOpenLicense() {
        return isOpenLicense;
    }
}
