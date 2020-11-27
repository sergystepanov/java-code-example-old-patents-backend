package ru.ineureka.patents.office.mapper

import ru.ineureka.patents.office.dto.PatentExtendedDto
import ru.ineureka.patents.office.dto.PropertyApplication

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JsonMapper implements PropertyMapper {

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Override
    PatentExtendedDto map(Object root) {
        return new PatentExtendedDto.PatentExtendedDtoBuilder()
                .grant_no(root.grant_no)
                .nation(root.nation)
                .actualDate(fromIsoDate(root.actual_date))
                .status(root.status)
                .fee_message(root.fee_message)
                .fee_year(root.fee_year)
                .annuity(root.annuity)
                .feeFromDate(fromIsoDate(root.fee_from_date))
                .feeToDate(fromIsoDate(root.fee_to_date))
                .pctDate(fromIsoDate(root.pct_date))
                .pct_application_number(root.pct_application_number)
                .pctApplicationDate(fromIsoDate(root.pct_application_date))
                .pct_publication_number(root.pct_publication_number)
                .pctPublicationDate(fromIsoDate(root.pct_publication_date))
                .startDate(fromIsoDate(root.start_date))
                .grantDate(fromIsoDate(root.grant_date))
                .application_no_full(root.application_no_full)
                .application_no(root.application_no)
                .application_no_ex(root.application_no_ex)
                .gracePeriod(fromIsoDate(root.grace_period))
                .admissionDate(fromIsoDate(root.admission_date))
                .dueDate(fromIsoDate(root.due_date))
                .proprietors(root.proprietors)
                .corrections(root.corrections)
                .domestic_appln_no(root.domestic_appln_no)
                .parentApplication(root.parent_application_number != null ? new PropertyApplication(
                        root.parent_application_number,
                        fromIsoDate(root.parent_application_date),
                        root.parent_application_link) : null
                )
                .build()
    }

    def fromIsoDate(String text) {
        text == null || text.isEmpty() ? null : LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
    }
}
