package ru.ineureka.patents.service.office.rupto.maintenance

import ru.ineureka.patents.service.office.rupto.maintenance.document.DesignMaintenanceFeeDocxDocument
import ru.ineureka.patents.service.office.rupto.maintenance.document.InventionMaintenanceFeeDocument
import ru.ineureka.patents.service.office.rupto.maintenance.document.UtilityMaintenanceFeeDocument
import ru.ineureka.patents.service.property.type.PropertyType
import spock.lang.Specification
import spock.lang.Unroll

import static ru.ineureka.patents.service.property.type.PropertyType.*

class MaintenanceFeeDocumentFactorySpec extends Specification {

    @Unroll
    def "if document type factory behaves correctly for supported property type: #type"() {
        when: 'a document is being selected by input property value'
        def document = MaintenanceFeeDocumentFactory.get(makeParams(type))

        then: 'the result document should be one of the supported'
        document.getClass() == result

        and: 'there is no exception thrown'
        noExceptionThrown()

        where:
        type    || result
        PATENT  || InventionMaintenanceFeeDocument
        UTILITY || UtilityMaintenanceFeeDocument
        DESIGN  || DesignMaintenanceFeeDocxDocument
    }

    @Unroll
    def "if document type factory behaves correctly for unsupported property type: #type"() {
        when: 'a document is being selected by input property value'
        MaintenanceFeeDocumentFactory.get(makeParams(type))

        then: 'exception should be thrown due to invalid or unsupported property type'
        thrown IllegalArgumentException

        where:
        type << [null as PropertyType]
    }

    @Unroll
    def "if DESIGN document template is selected properly for #template"() {
        when: 'a document is being selected by input property value'
        def document = MaintenanceFeeDocumentFactory.get(
                new DocumentParams.Builder()
                        .withDocumentDate(documentDate)
                        .withPropertyApplicationDate(applicationDate)
                        .withPropertyStartDate(startDate)
                        .withPropertyType(type)
                        .build()
        )

        then: 'the result document should be one of the supported'
        template == null ? document.getTemplateUrl() == null : document.getTemplateUrl().toString().endsWith(template)

        and: 'there is no exception thrown'
        noExceptionThrown()

        where:
        documentDate | applicationDate | startDate    | type   || template
        '2021-01-01' | '2006-01-01'    | '2006-01-01' | DESIGN || 'fips_renewal_design_001.tpl.docx'
        '2020-01-01' | '2006-01-01'    | '2006-01-01' | DESIGN || 'fips_renewal_design_001.tpl.docx'
        '2019-12-31' | '2006-01-01'    | '2006-01-01' | DESIGN || null
        '2021-05-13' | '2015-01-01'    | '2015-01-01' | DESIGN || 'fips_renewal_design_003.tpl.docx'
        '2021-01-02' | '2006-01-01'    | '2006-01-01' | DESIGN || 'fips_renewal_design_002.tpl.docx'
        '2021-07-01' | '2006-01-01'    | '2006-01-01' | DESIGN || 'fips_renewal_design_002.tpl.docx'
    }

    def makeParams(PropertyType type) {
        new DocumentParams.Builder()
                .withDocumentDate('2020-01-01')
                .withPropertyApplicationDate('2010-01-01')
                .withPropertyStartDate('2010-01-01')
                .withPropertyType(type)
                .build()
    }
}
