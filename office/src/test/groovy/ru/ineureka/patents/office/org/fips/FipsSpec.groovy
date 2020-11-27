package ru.ineureka.patents.office.org.fips

import ru.ineureka.patents.office.Store
import ru.ineureka.patents.office.dto.PatentExtendedDto
import ru.ineureka.patents.office.exception.OfficeError
import ru.ineureka.patents.office.exception.PatentOfficeException
import ru.ineureka.patents.office.mapper.JsonMapper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FipsSpec extends Specification {

    @Shared
    def store = new Store<Fips>()
            .withArchivePath('fips/html/v2/fips_v2_archive.zip')
            .withJson('fips/json/v2')
            .withDocumentConstructor({ fileName, data -> new Fips(fileName.substring(0, 1), data) })
            .build()

    @Shared
    def mapper = new JsonMapper()

    @Unroll
    def "if random set of documents is parsed correctly for #fileName"() {
        when: "given next property from the store"
        def property = store.getPatent fileName

        then: "the property has some marker that it's ok"
        !property.application_no.isBlank() || !property.grant_no.isBlank()

        where:
        fileName << store.getRandomListOfDocuments(15)
    }

    @Unroll
    def "if parsing works for the latest doc snapshots -> #fileName"() {
        when:
        def fromJson = mapper.map store.getJson(fileName)
        def fromHtml = store.getPatent fileName

        then:
        fromJson.grant_no == fromHtml.grant_no
        fromJson.nation == fromHtml.nation
        fromJson.actualDate == fromHtml.actualDate
        fromJson.status == fromHtml.status
        fromJson.fee_message == fromHtml.fee_message
        fromJson.fee_year == fromHtml.fee_year
        fromJson.annuity == fromHtml.annuity
        fromJson.feeFromDate == fromHtml.feeFromDate
        fromJson.feeToDate == fromHtml.feeToDate
        fromJson.pctDate == fromHtml.pctDate
        fromJson.pct_application_number == fromHtml.pct_application_number
        fromJson.pctApplicationDate == fromHtml.pctApplicationDate
        fromJson.pct_publication_number == fromHtml.pct_publication_number
        fromJson.pctPublicationDate == fromHtml.pctPublicationDate
        fromJson.startDate == fromHtml.startDate
        fromJson.grantDate == fromHtml.grantDate
        fromJson.application_no_full == fromHtml.application_no_full
        fromJson.application_no == fromHtml.application_no
        fromJson.application_no_ex == fromHtml.application_no_ex
        fromJson.gracePeriod == fromHtml.gracePeriod
        fromJson.admissionDate == fromHtml.admissionDate
        fromJson.dueDate == fromHtml.dueDate
        fromJson.proprietors == fromHtml.proprietors
        fromJson.corrections == fromHtml.corrections
        fromJson.domestic_appln_no == fromHtml.domestic_appln_no
        fromJson.parentApplication?.number == fromHtml.parentApplication?.number

        where:
        fileName << [
                'd48603', 'd84445', 'd96959', 'p2201459', 'p2211248', 'p2237365', 'p2380878', 'p2434147', 'p2434354',
                'p2584593', 'p2603539', 'u117877'
        ]
    }

    @Unroll
    def "if FipsException is being thrown properly for #fileName"() {
        when:
        store.getPatent 'error/' + fileName

        then:
        def ex = thrown PatentOfficeException
        ex.errorCode == error

        where:
        fileName              || error
        'error'               || OfficeError.LIMIT_EXCEED
        'too_quick'           || OfficeError.TOO_QUICK
        'error_docnotfound_1' || OfficeError.NAMED
        'fips_generic_error'  || OfficeError.NAMED
        'fips_unavail'        || OfficeError.NAMED
    }

    @Unroll
    def "if property has correct status for #fileName"() {
        expect:
        store.getPatent(fileName).status == status

        where:
        fileName | status
        'd96940' | 'active'
    }

    @Unroll
    def "if property has correct due date value for #fileName"() {
        expect:
        store.getPatent(fileName).dueDate == fromIsoDate(dueDate)

        where:
        fileName      | dueDate
        // Empty
        'p2014103427' | null
        // Admission date and application date (_), (22)
        'p2014102029' | '2014-01-22'
        // Application date only (22)
        'p2010137820' | '2008-02-13'
        // Both (22) and (24) on patent (D) type
        'd78750'      | '2018-03-04'
        'd78865'      | '2018-03-04'
        // Getting of corrections
        'p2428353'    | '2017-03-21'
        // February 29/28 days years
        'p2434147'    | '2018-02-28'
        'd84445'      | '2017-02-28'
    }

    @Unroll
    def "if corrections blocks are parsed as well in #fileName"() {
        when: 'a patent document with corrections is parsed'
        def document = store.getDocument(fileName) as Fips
        def property = document.patent

        then: 'property has corrections'
        property.corrections

        and: 'property has not empty corrections'
        !property.corrections_text.isBlank()

        and: 'proprietors correspond to corrected values'
        property.proprietors == proprietors

        and: 'processed document contains corrections block marker'
        document.tidyHtml().contains(code)

        where:
        fileName   || code   | proprietors
        'd75437'   || 'PD4L' | ['Булгари Орложери СА (CH)']
        'd75997'   || 'PD4L' | ['Булгари Орложери СА (CH)']
        'd78962'   || 'PD4L' | ['Булгари Орложери СА (CH)']
        'p2325047' || 'MM4A' | ['БОТЕМ ЭЛЕКТРОНИК КО., ЛТД. (KR)']
        'p2390818' || '' | ['БСХ ХАУСГЕРЕТЕ ГМБХ (DE)']
    }

    @Unroll
    def "if PCT dates are parsed correctly for #fileName"() {
        expect:
        store.getPatent(fileName).pctDate == fromIsoDate(pctDate)

        where:
        fileName   | pctDate
        'p2401099' | '2008-05-12'
        'p2346402' | '2007-11-02'
    }

    @Unroll
    def "if proprietors with parenthesis in the name are parsed for #fileName"() {
        when:
        def parsed = store.getPatent(fileName).proprietors

        then:
        parsed.size() > 0

        and:
        parsed == proprietors

        where:
        fileName   | proprietors
        'd77434'   | ['ВАНДЕРЛЭНД НЁСЕРИГУДС КО., ЛТД. (TW)']
        'p2380878' | ['РЕПАБЛИК ОФ КОРЕЯ (МЕНЕДЖМЕНТ: РУРАЛ ДЕВЕЛОПМЕНТ АДМИНИСТРЕЙШН) (KR)']
        'p2397521' | ['ВИД, Гюнтер (DE)', 'Иномитек ГмбХ унд Ко. КГ (DE)']
        'p2435190' | ['ЭРБЮС ОПЕРАСЬОН (САС) (FR)', 'ЭЙРБАС ОПЕРЕЙШНЗ ЛИМИТЕД (GB)']
    }

    @Unroll
    def "if convention priorities are parsed correctly for #fileName"() {
        expect:
        store.getPatent(fileName).domestic_appln_no == domestics

        where:
        fileName   | domestics
        'p2538453' | [
                '28.05.2009 KR 10-2009-0047192', '27.08.2009 KR 10-2009-0079950', '24.05.2010 KR 10-2010-0047877']
        'd90933'   | ['20.06.2013 KR 30-2013-0031765']
        'p2541782' | ['20.10.2010 KR 10-2010-0102665']
        'p2229922' | ['31.12.1998 ( (пп.1-12) ) US 09/223,885']
    }

    @Unroll
    def "if PCT data is parsed correctly for #fileName"() {
        when:
        def property = store.getPatent fileName

        then:
        property.pctDate == fromIsoDate(pctDate)
        property.pct_application_number == pctApplicationNumber
        property.pctApplicationDate == fromIsoDate(pctApplicationDate)
        property.pct_publication_number == pctPublicationNumber
        property.pctPublicationDate == fromIsoDate(pctPublicationDate)

        where:
        fileName   | pctDate      | pctApplicationNumber | pctApplicationDate | pctPublicationNumber | pctPublicationDate
        'p2360913' | '2005-12-30' | 'CA 2004/000793'     | '2004-05-28'       | 'WO 2004/106328'     | '2004-12-09'
    }

    @Unroll
    def "if fancy application numbers are parsed for #fileName"() {
        when:
        def property = store.getPatent fileName

        then:
        property.application_no == number
        property.application_no_ex == exNumber

        where:
        fileName   | number       | exNumber
        'p2229922' | '2001121313' | '15'
    }

    @Unroll
    def "if start dates are parsed correctly for #fileName"() {
        expect:
        store.getPatent(fileName).startDate == fromIsoDate(startDate)

        where:
        fileName      | startDate
        'p2014103427' | null
        'p2010137820' | '2008-02-13'
        'd78750'      | '2010-03-04'
        'p2428353'    | '2006-03-27'
    }

    @Unroll
    def "if application numbers are parsed correctly for #fileName"() {
        expect:
        store.getPatent(fileName).application_no == applicationNumber

        where:
        fileName | applicationNumber
        'd47700' | '98500747'
    }

    @Unroll
    def "if open license is parsed correctly for #fileName"() {
        when:
        def property = store.getPatent fileName

        then:
        property.open_license == isOpenLicense
        property.openLicenseRegDate == fromIsoDate(openLicenseDate)

        where:
        fileName   | isOpenLicense | openLicenseDate
        'p2211248' | true          | '2014-01-27'
        'p2201459' | true          | '2014-01-27'
    }

    @Unroll
    def "if property data from its application is correct for #fileName"() {
        when:
        def property = store.getPatent fileName

        then:
        property.application_no == applicationNumber
        property.grant_no == grantNumber
        property.processPublication == isProcessPublication

        where:
        fileName       | applicationNumber | grantNumber | isProcessPublication
        'pa2013113952' | '2013113952'      | '2514927'   | true
    }

    @Unroll
    def "if property status is parsed correctly for #type ##number"() {
        expect: 'that parsed status is what expected'
        store.getPatent(type + number).status == status

        where: 'property attributes are these'
        type | number    || status
        'p'  | '2325047' || 'terminated_can_be_recovered'
        'p'  | '2480212' || 'can_be_terminated'
        'p'  | '2480409' || 'active'
        'p'  | '2480433' || 'terminated_can_be_recovered'
        'p'  | '2480460' || 'inactive'
    }

    @Unroll
    def "if url getter is correct for type: #type"() {
        expect:
        Fips.getUrl(type, '1000').contains(resultHas)

        where:
        type      || resultHas
        'p'       || 'RUPAT'
        'u'       || 'RUPM'
        'd'       || 'RUDE'
        'P'       || 'RUPAT'
        'U'       || 'RUPM'
        'D'       || 'RUDE'
        't'       || 'RUPAT'
        'garbage' || 'RUPAT'
    }

    def "if url getter fail on invalid input params"() {
        when:
        Fips.getUrl(type, number)

        then:
        thrown(NullPointerException)

        where:
        type | number
        null | null
        null | '100'
        'p'  | null
    }

    def propertyToList(PatentExtendedDto p) {
        [
                p.grant_no, p.nation, p.actualDate,
                p.status_description, p.status,
                p.fee_message, p.fee_year, p.annuity, p.feeFromDate, p.feeToDate,
                p.pctDate, p.pct_application_number, p.pctApplicationDate,
                p.pct_publication_number, p.pctPublicationDate,
                p.startDate, p.grantDate, p.application_no_full, p.application_no,
                p.application_no_ex, p.gracePeriod, p.admissionDate,
                p.dueDate, p.proprietors, p.corrections, p.domestic_appln_no,
                p.parentApplication
        ]
    }

    def fromIsoDate(String text) {
        text == null || text.isEmpty() ? null : LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
    }
}
