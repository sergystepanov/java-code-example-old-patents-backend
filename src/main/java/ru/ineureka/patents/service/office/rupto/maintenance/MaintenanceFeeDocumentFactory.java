package ru.ineureka.patents.service.office.rupto.maintenance;

import ru.ineureka.patents.service.office.rupto.maintenance.document.DesignMaintenanceFeeDocxDocument;
import ru.ineureka.patents.service.office.rupto.maintenance.document.InventionMaintenanceFeeDocument;
import ru.ineureka.patents.service.office.rupto.maintenance.document.MaintenanceFeeDocument;
import ru.ineureka.patents.service.office.rupto.maintenance.document.UtilityMaintenanceFeeDocument;

import java.time.LocalDate;
import java.util.Objects;

public final class MaintenanceFeeDocumentFactory {

    public static MaintenanceFeeDocument get(DocumentParams params) {
        final var type = params.getPropertyType();

        if (Objects.nonNull(type)) {
            switch (type) {
                case PATENT:
                    return new InventionMaintenanceFeeDocument();
                case UTILITY:
                    return new UtilityMaintenanceFeeDocument();
                case DESIGN:
                    var document = new DesignMaintenanceFeeDocxDocument();
                    final var startDate = params.getPropertyStartDate();
                    final var applicationDate = params.getPropertyApplicationDate();
                    final var documentDate = params.getDocumentDate();

                    var templateName = "";
                    if (isPublishedAfterYear2015Included(applicationDate)) {
                        templateName = "/templates/fips_renewal_design_003.tpl.docx";
                    } else {
                        if (isDocumentDuringFifteenthYear(documentDate, startDate)) {
                            templateName = "/templates/fips_renewal_design_001.tpl.docx";
                        } else {
                            if (isDocumentAfterFifteenthYearGracePeriod(documentDate, startDate)) {
                                templateName = "/templates/fips_renewal_design_002.tpl.docx";
                            }
                        }
                    }

                    if (!templateName.isEmpty()) {
                        document.withTemplate(params.getClass().getResource(templateName));
                    }

                    return document;
            }
        }
        throw new IllegalArgumentException("Unsupported or undefined property type: " + type);
    }

    private static boolean isPublishedAfterYear2015Included(LocalDate date) {
        return date.isAfter(LocalDate.of(2014, 12, 31));
    }

    private static boolean isDocumentDuringFifteenthYear(LocalDate documentDate, LocalDate startDate) {
        return documentDate.isAfter(startDate.plusYears(14).minusDays(1)) &&
                documentDate.isBefore(startDate.plusYears(15).plusDays(1));
    }

    private static boolean isDocumentAfterFifteenthYearGracePeriod(LocalDate documentDate, LocalDate startDate) {
        return documentDate.isAfter(startDate.plusYears(15)) &&
                documentDate.isBefore(startDate.plusYears(15).plusMonths(6).plusDays(1));
    }

    private MaintenanceFeeDocumentFactory() {
    }
}
