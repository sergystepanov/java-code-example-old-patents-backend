package ru.ineureka.patents.service.office.rupto.maintenance;

import ru.ineureka.patents.service.property.type.PropertyType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DocumentParams {
    private final PropertyType propertyType;
    private final LocalDate propertyApplicationDate;
    private final LocalDate propertyStartDate;
    private final LocalDate documentDate;

    public DocumentParams(Builder builder) {
        this.propertyType = builder.propertyType;
        this.propertyApplicationDate = builder.propertyApplicationDate;
        this.propertyStartDate = builder.propertyStartDate;
        this.documentDate = builder.documentDate;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public LocalDate getPropertyApplicationDate() {
        return propertyApplicationDate;
    }

    public LocalDate getPropertyStartDate() {
        return propertyStartDate;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public static final class Builder {
        private PropertyType propertyType;
        private LocalDate propertyApplicationDate;
        private LocalDate propertyStartDate;
        private LocalDate documentDate;

        public Builder withPropertyType(PropertyType type) {
            this.propertyType = type;
            return this;
        }

        public Builder withPropertyApplicationDate(String applicationDate) {
            this.propertyApplicationDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(applicationDate));
            return this;
        }

        public Builder withPropertyStartDate(String startDate) {
            this.propertyStartDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(startDate));
            return this;
        }

        public Builder withDocumentDate(String documentDate) {
            this.documentDate = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(documentDate));
            return this;
        }

        public DocumentParams build() {
            return new DocumentParams(this);
        }
    }
}
