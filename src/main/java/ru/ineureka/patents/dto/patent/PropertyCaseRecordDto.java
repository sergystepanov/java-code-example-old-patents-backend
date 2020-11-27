package ru.ineureka.patents.dto.patent;

public final class PropertyCaseRecordDto {
    private final long id;
    private final long property_case_id;
    private final long property_id;
    private final boolean checked;
    private final long payment_office;
    private final String payment_country;
    private final String payment_deadline;
    private final int payment_annuity;
    private final int payment_annuity_years;
    private final double payment_amount;
    private final double payment_late_fine;
    private final double payment_conversion_course;
    private final double payment_profit_amount;
    private final long payment_covering_letter_file;
    private final long payment_export_file;
    private final boolean payed_to;
    private final boolean payed_in;

    public PropertyCaseRecordDto(long id, long property_case_id, long property_id, boolean checked, long payment_office,
                                 String payment_country, String payment_deadline, int payment_annuity,
                                 int payment_annuity_years,
                                 double payment_amount, double payment_late_fine, double payment_conversion_course,
                                 double payment_profit_amount, long payment_covering_letter_file,
                                 long payment_export_file, boolean payed_to, boolean payed_in) {
        this.id = id;
        this.property_case_id = property_case_id;
        this.property_id = property_id;
        this.checked = checked;
        this.payment_office = payment_office;
        this.payment_country = payment_country;
        this.payment_deadline = payment_deadline;
        this.payment_annuity = payment_annuity;
        this.payment_annuity_years = payment_annuity_years;
        this.payment_amount = payment_amount;
        this.payment_late_fine = payment_late_fine;
        this.payment_conversion_course = payment_conversion_course;
        this.payment_profit_amount = payment_profit_amount;
        this.payment_covering_letter_file = payment_covering_letter_file;
        this.payment_export_file = payment_export_file;
        this.payed_to = payed_to;
        this.payed_in = payed_in;
    }

    public long getId() {
        return id;
    }

    public long getProperty_case_id() {
        return property_case_id;
    }

    public long getProperty_id() {
        return property_id;
    }

    public boolean isChecked() {
        return checked;
    }

    public long getPayment_office() {
        return payment_office;
    }

    public String getPayment_country() {
        return payment_country;
    }

    public String getPayment_deadline() {
        return payment_deadline;
    }

    public int getPayment_annuity() {
        return payment_annuity;
    }

    public int getPayment_annuity_years() {
        return payment_annuity_years;
    }

    public double getPayment_amount() {
        return payment_amount;
    }

    public double getPayment_late_fine() {
        return payment_late_fine;
    }

    public double getPayment_conversion_course() {
        return payment_conversion_course;
    }

    public double getPayment_profit_amount() {
        return payment_profit_amount;
    }

    public long getPayment_covering_letter_file() {
        return payment_covering_letter_file;
    }

    public long getPayment_export_file() {
        return payment_export_file;
    }

    public boolean isPayed_to() {
        return payed_to;
    }

    public boolean isPayed_in() {
        return payed_in;
    }
}
