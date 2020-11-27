package ru.ineureka.patents.dto.patent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

public final class PropertyDto {
    private final long id;
    private final String registration_number;
    private final long type;
    private final long patent_office;
    private final String nation;
    private final String application_no;
    private final String application_no_ex;
    private final String application_date;
    private final String grant_date;
    private final String grace_period;
    private final String expiry_date;
    private final String description;
    private final String status;
    private final boolean open_license;
    private List<PropertyOwnerDto> owners;

    public PropertyDto(long id, String registration_number, long type,
                       long patent_office, String nation,
                       String application_no, String application_no_ex, String application_date,
                       String grant_date, String grace_period, String expiry_date,
                       String description, String status, boolean open_license) {
        this.id = id;
        this.registration_number = registration_number;
        this.type = type;
        this.patent_office = patent_office;
        this.nation = nation;
        this.application_no = application_no;
        this.application_no_ex = application_no_ex;
        this.application_date = application_date;
        this.grant_date = grant_date;
        this.grace_period = grace_period;
        this.expiry_date = expiry_date;
        this.description = description;
        this.status = status;
        this.open_license = open_license;
    }

    public long getId() {
        return this.id;
    }

    public String getRegistration_number() {
        return this.registration_number;
    }

    public long getType() {
        return this.type;
    }

    public long getPatent_office() {
        return this.patent_office;
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

    public String getApplication_date() {
        return this.application_date;
    }

    public String getGrant_date() {
        return this.grant_date;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isOpen_license() {
        return this.open_license;
    }

    public List<PropertyOwnerDto> getOwners() {
        return this.owners;
    }

    public void setOwners(List<PropertyOwnerDto> owners) {
        this.owners = owners;
    }

    public String getGrace_period() {
        return grace_period;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    @Override
    public String toString() {
        return "PropertyDto{" +
                "id=" + id +
                ", registration_number='" + registration_number + '\'' +
                ", type=" + type +
                ", patent_office=" + patent_office +
                ", nation='" + nation + '\'' +
                ", application_no='" + application_no + '\'' +
                ", application_no_ex='" + application_no_ex + '\'' +
                ", application_date='" + application_date + '\'' +
                ", grant_date='" + grant_date + '\'' +
                ", grace_period='" + grace_period + '\'' +
                ", expiry_date='" + expiry_date + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", open_license=" + open_license +
                ", owners=" + owners +
                '}';
    }

    @JsonIgnore
    public String getNumber() {
        if (this.registration_number != null && !Objects.equals(this.registration_number, "")) {
            return this.registration_number;
        }

        if (this.application_no != null && !Objects.equals(this.application_no, "")) {
            return this.application_no;
        }

        return "";
    }
}
