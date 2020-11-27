package ru.ineureka.patents.office.dto;

import java.time.LocalDate;
import java.util.List;

public class PatentExtendedDto {
    private final long id;
    private final String grant_no;
    private final String type;
    private final String type_name;
    private final String registry;
    private final String nation;
    private final String application_no;
    private final String application_no_ex;
    private final LocalDate applicationDate;
    private final LocalDate grantDate;
    private final String description;
    private final String status;
    private LocalDate actualDate;
    private LocalDate admissionDate;
    private String annuity;
    private String application_no_full;
    private boolean corrections;
    private String corrections_text;
    private boolean open_license;
    private LocalDate openLicenseRegDate;
    private List<String> domestic_appln_no;
    private LocalDate dueDate;
    private LocalDate feeFromDate;
    private String fee_message;
    private LocalDate feeToDate;
    private String fee_year;
    private LocalDate gracePeriod;
    private LocalDate pctDate;
    private String pct_application_number;
    private LocalDate pctApplicationDate;
    private String pct_publication_number;
    private LocalDate pctPublicationDate;
    private List<String> proprietors;
    private String pub_date;
    private LocalDate startDate;
    private String status_description;
    private LocalDate expiryDate;
    private PropertyApplication parentApplication;
    private boolean due_date_calculated;
    private boolean isPublished;
    private boolean isCached;
    private boolean processPublication;

    PatentExtendedDto(long id, String grant_no, String type, String type_name, String registry,
                      String nation, String application_no, String application_no_ex,
                      LocalDate applicationDate, LocalDate grantDate, String description,
                      String status, LocalDate actualDate, LocalDate admissionDate,
                      String annuity, String application_no_full,
                      boolean corrections, String corrections_text, boolean open_license,
                      LocalDate openLicenseRegDate, List<String> domestic_appln_no, LocalDate dueDate,
                      LocalDate feeFromDate, String fee_message, LocalDate feeToDate, String fee_year,
                      LocalDate gracePeriod, LocalDate pctDate, String pct_application_number,
                      LocalDate pctApplicationDate, String pct_publication_number,
                      LocalDate pctPublicationDate, List<String> proprietors,
                      String pub_date, LocalDate startDate, String status_description, LocalDate expiryDate,
                      PropertyApplication parentApplication, boolean due_date_calculated, boolean isPublished,
                      boolean isCached, boolean processPublication) {
        this.id = id;
        this.grant_no = grant_no;
        this.type = type;
        this.type_name = type_name;
        this.registry = registry;
        this.nation = nation;
        this.application_no = application_no;
        this.application_no_ex = application_no_ex;
        this.applicationDate = applicationDate;
        this.grantDate = grantDate;
        this.description = description;
        this.status = status;
        this.actualDate = actualDate;
        this.admissionDate = admissionDate;
        this.annuity = annuity;
        this.application_no_full = application_no_full;
        this.corrections = corrections;
        this.corrections_text = corrections_text;
        this.open_license = open_license;
        this.openLicenseRegDate = openLicenseRegDate;
        this.domestic_appln_no = domestic_appln_no;
        this.dueDate = dueDate;
        this.feeFromDate = feeFromDate;
        this.fee_message = fee_message;
        this.feeToDate = feeToDate;
        this.fee_year = fee_year;
        this.gracePeriod = gracePeriod;
        this.pctDate = pctDate;
        this.pct_application_number = pct_application_number;
        this.pctApplicationDate = pctApplicationDate;
        this.pct_publication_number = pct_publication_number;
        this.pctPublicationDate = pctPublicationDate;
        this.proprietors = proprietors;
        this.pub_date = pub_date;
        this.startDate = startDate;
        this.status_description = status_description;
        this.expiryDate = expiryDate;
        this.parentApplication = parentApplication;
        this.due_date_calculated = due_date_calculated;
        this.isPublished = isPublished;
        this.isCached = isCached;
        this.processPublication = processPublication;
    }

    public static PatentExtendedDtoBuilder builder() {
        return new PatentExtendedDtoBuilder();
    }

    public long getId() {
        return this.id;
    }

    public String getGrant_no() {
        return this.grant_no;
    }

    public String getType() {
        return this.type;
    }

    public String getType_name() {
        return this.type_name;
    }

    public String getRegistry() {
        return this.registry;
    }

    public String getNation() {
        return this.nation;
    }

    public String getApplication_no() {
        return this.application_no;
    }

    public String getApplication_no_ex() {
        return this.application_no_ex;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public LocalDate getGrantDate() {
        return this.grantDate;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDate getActualDate() {
        return this.actualDate;
    }

    public LocalDate getAdmissionDate() {
        return this.admissionDate;
    }

    public String getAnnuity() {
        return this.annuity;
    }

    public String getApplication_no_full() {
        return this.application_no_full;
    }

    public boolean isCorrections() {
        return this.corrections;
    }

    public String getCorrections_text() {
        return this.corrections_text;
    }

    public boolean isOpen_license() {
        return this.open_license;
    }

    public LocalDate getOpenLicenseRegDate() {
        return this.openLicenseRegDate;
    }

    public List<String> getDomestic_appln_no() {
        return this.domestic_appln_no;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public LocalDate getFeeFromDate() {
        return this.feeFromDate;
    }

    public String getFee_message() {
        return this.fee_message;
    }

    public LocalDate getFeeToDate() {
        return this.feeToDate;
    }

    public String getFee_year() {
        return this.fee_year;
    }

    public LocalDate getGracePeriod() {
        return this.gracePeriod;
    }

    public LocalDate getPctDate() {
        return this.pctDate;
    }

    public String getPct_application_number() {
        return this.pct_application_number;
    }

    public LocalDate getPctApplicationDate() {
        return this.pctApplicationDate;
    }

    public String getPct_publication_number() {
        return this.pct_publication_number;
    }

    public LocalDate getPctPublicationDate() {
        return this.pctPublicationDate;
    }

    public List<String> getProprietors() {
        return this.proprietors;
    }

    public String getPub_date() {
        return this.pub_date;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public String getStatus_description() {
        return this.status_description;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public PropertyApplication getParentApplication() {
        return parentApplication;
    }

    public boolean isDue_date_calculated() {
        return this.due_date_calculated;
    }

    public boolean isPublished() {
        return this.isPublished;
    }

    public boolean isCached() {
        return this.isCached;
    }

    public boolean isProcessPublication() {
        return processPublication;
    }

    public String toString() {
        return "PatentExtendedDto(id=" + this.getId() + ", grant_no=" + this.getGrant_no() +
                ", type=" + this.getType() + ", type_name=" + this.getType_name() +
                ", registry=" + this.getRegistry() + ", nation=" + this.getNation() +
                ", application_no=" + this.getApplication_no() +
                ", application_no_ex=" + this.getApplication_no_ex() +
                ", application_date=" + this.getApplicationDate() + ", grant_date=" + this.getGrantDate() +
                ", description=" + this.getDescription() + ", status=" + this.getStatus() +
                ", actual_date=" + this.getActualDate() + ", admission_date=" + this.getAdmissionDate() +
                ", annuity=" + this.getAnnuity() + ", application_no_full=" + this.getApplication_no_full() +
                ", corrections=" + this.isCorrections() + ", corrections_text=" + this.getCorrections_text() +
                ", open_license=" + this.isOpen_license() +
                ", open_license_reg_date=" + this.getOpenLicenseRegDate() +
                ", domestic_appln_no=" + this.getDomestic_appln_no() + ", due_date=" + this.getDueDate() +
                ", fee_from_date=" + this.getFeeFromDate() + ", fee_message=" + this.getFee_message() +
                ", FeeToDate=" + this.getFeeToDate() + ", fee_year=" + this.getFee_year() +
                ", grace_period=" + this.getGracePeriod() + ", pct_date=" + this.getPctDate() +
                ", pct_application_number=" + this.getPct_application_number() +
                ", pct_application_date=" + this.getPctApplicationDate() +
                ", pct_publication_number=" + this.getPct_publication_number() +
                ", pct_publication_date=" + this.getPctPublicationDate() +
                ", proprietors=" + this.getProprietors() + ", pub_date=" + this.getPub_date() +
                ", start_date=" + this.getStartDate() +
                ", status_description=" + this.getStatus_description() +
                ", expiryDate=" + this.getExpiryDate() +
                ", parent_application=" + this.getParentApplication() +
                ", due_date_calculated=" + this.isDue_date_calculated() +
                ", isPublished=" + this.isPublished() +
                ", isCached=" + this.isCached() +
                ", isProcessPublication=" + this.processPublication + ")";
    }

    public static class PatentExtendedDtoBuilder {
        private long id;
        private String grant_no;
        private String type;
        private String type_name;
        private String registry;
        private String nation;
        private String application_no;
        private String application_no_ex;
        private LocalDate applicationDate;
        private LocalDate grantDate;
        private String description;
        private String status;
        public LocalDate actualDate;
        private LocalDate admissionDate;
        private String annuity;
        private String application_no_full;
        private boolean corrections;
        private String corrections_text;
        private boolean open_license;
        private LocalDate openLicenseRegDate;
        private List<String> domestic_appln_no;
        private LocalDate dueDate;
        private LocalDate feeFromDate;
        private String fee_message;
        private LocalDate feeToDate;
        private String fee_year;
        private LocalDate gracePeriod;
        private LocalDate pctDate;
        private String pct_application_number;
        private LocalDate pctApplicationDate;
        private String pct_publication_number;
        private LocalDate pctPublicationDate;
        private List<String> proprietors;
        private String pub_date;
        private LocalDate startDate;
        private String status_description;
        private LocalDate expiryDate;
        private PropertyApplication parentApplication;
        private boolean due_date_calculated;
        private boolean isPublished;
        private boolean isCached;
        private boolean isProcessPublication;

        public PatentExtendedDtoBuilder() {
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder id(long id) {
            this.id = id;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder grant_no(String grant_no) {
            this.grant_no = grant_no;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder type(String type) {
            this.type = type;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder type_name(String type_name) {
            this.type_name = type_name;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder registry(String registry) {
            this.registry = registry;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder nation(String nation) {
            this.nation = nation;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder application_no(String application_no) {
            this.application_no = application_no;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder application_no_ex(String application_no_ex) {
            this.application_no_ex = application_no_ex;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder applicationDate(LocalDate applicationDate) {
            this.applicationDate = applicationDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder grantDate(LocalDate grantDate) {
            this.grantDate = grantDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder actualDate(LocalDate actualDate) {
            this.actualDate = actualDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder admissionDate(LocalDate admissionDate) {
            this.admissionDate = admissionDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder annuity(String annuity) {
            this.annuity = annuity;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder application_no_full(String application_no_full) {
            this.application_no_full = application_no_full;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder corrections(boolean corrections) {
            this.corrections = corrections;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder corrections_text(String corrections_text) {
            this.corrections_text = corrections_text;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder open_license(boolean open_license) {
            this.open_license = open_license;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder openLicenseRegDate(LocalDate openLicenseRegDate) {
            this.openLicenseRegDate = openLicenseRegDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder domestic_appln_no(List<String> domestic_appln_no) {
            this.domestic_appln_no = domestic_appln_no;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder feeFromDate(LocalDate feeFromDate) {
            this.feeFromDate = feeFromDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder fee_message(String fee_message) {
            this.fee_message = fee_message;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder feeToDate(LocalDate feeToDate) {
            this.feeToDate = feeToDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder fee_year(String fee_year) {
            this.fee_year = fee_year;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder gracePeriod(LocalDate gracePeriod) {
            this.gracePeriod = gracePeriod;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder pctDate(LocalDate pctDate) {
            this.pctDate = pctDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder pct_application_number(String pct_application_number) {
            this.pct_application_number = pct_application_number;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder pctApplicationDate(LocalDate pctApplicationDate) {
            this.pctApplicationDate = pctApplicationDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder pct_publication_number(String pct_publication_number) {
            this.pct_publication_number = pct_publication_number;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder pctPublicationDate(LocalDate pctPublicationDate) {
            this.pctPublicationDate = pctPublicationDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder proprietors(List<String> proprietors) {
            this.proprietors = proprietors;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder pub_date(String pub_date) {
            this.pub_date = pub_date;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder status_description(String status_description) {
            this.status_description = status_description;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder parentApplication(PropertyApplication parentApplication) {
            this.parentApplication = parentApplication;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder due_date_calculated(boolean due_date_calculated) {
            this.due_date_calculated = due_date_calculated;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder isPublished(boolean isPublished) {
            this.isPublished = isPublished;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder isCached(boolean isCached) {
            this.isCached = isCached;
            return this;
        }

        public PatentExtendedDto.PatentExtendedDtoBuilder isProcessPublication(boolean isProcessPublication) {
            this.isProcessPublication = isProcessPublication;
            return this;
        }

        public PatentExtendedDto build() {
            return new PatentExtendedDto(id, grant_no, type, type_name, registry, nation, application_no,
                    application_no_ex, applicationDate, grantDate, description, status, actualDate,
                    admissionDate, annuity, application_no_full, corrections, corrections_text, open_license,
                    openLicenseRegDate, domestic_appln_no, dueDate, feeFromDate, fee_message, feeToDate,
                    fee_year, gracePeriod, pctDate, pct_application_number, pctApplicationDate,
                    pct_publication_number, pctPublicationDate, proprietors, pub_date, startDate,
                    status_description, expiryDate, parentApplication, due_date_calculated, isPublished, isCached,
                    isProcessPublication);
        }
    }
}
