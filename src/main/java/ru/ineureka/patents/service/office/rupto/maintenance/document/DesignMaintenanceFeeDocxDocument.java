package ru.ineureka.patents.service.office.rupto.maintenance.document;

public class DesignMaintenanceFeeDocxDocument extends MaintenanceFeeDocument {
    private static final String MIME = "application/msword";

    @Override
    public String getTemplateMimeType() {
        return MIME;
    }
}
