package ru.ineureka.patents.service.cases;

import java.util.Set;

public enum Fields {
    ID("id"),
    NATION("nation"),
    TYPE("type"),
    APPLICATION_NUMBER("application_number"),
    APPLICATION_DATE("application_date"),
    PARENT_APPLICATION_NUMBER("parent_application_number"),
    PARENT_APPLICATION_DATE("parent_application_date"),
    REGISTRATION_NUMBER("registration_number"),
    GRANT_DATE("grant_date"),
    GRACE_PERIOD("grace_period"),
    EXPIRY_DATE("expiry_date"),
    PCT_DATE("pct_date"),
    PROPRIETORS("proprietors"),
    DUE_DATE("due_date"),
    PAYMENT_ANNUITY("payment_annuity"),
    PAYMENT_ANNUITY_YEARS("payment_annuity_years"),
    PAYMENT_AMOUNT("payment_amount"),
    PAYMENT_LATE_FINE("payment_late_fine"),
    OFFICE("office"),
    OFFICE_FEE("office_fee"),
    DESCRIPTION("description"),
    OPEN_LICENSE("open_license"),
    CACHED("cached"),
    TYPE_ID("type_id");

    // Stores field values which represent dates
    public static final Set<String> valuesWithDates = Set.of(
            Fields.APPLICATION_DATE.toString(), Fields.DUE_DATE.toString(), Fields.GRANT_DATE.toString(),
            Fields.PARENT_APPLICATION_DATE.toString(), Fields.EXPIRY_DATE.toString(), Fields.GRACE_PERIOD.toString());

    // Stores field values which are mandatory for any documents
    public static final Set<String> requiredFields = Set.of(Fields.TYPE.toString(), Fields.PAYMENT_ANNUITY.toString());

    private final String text;

    Fields(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
