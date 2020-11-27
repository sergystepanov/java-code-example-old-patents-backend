package ru.ineureka.patents.service.office

import ru.ineureka.patents.persistence.office.PatentOfficeFee
import spock.lang.Specification

class OfficeServiceSpec extends Specification {

    def "if fees grouped by appliance date"() {
        given: 'the service'
        def service = new OfficeService(null, null)

        when: 'grouped fees are requested'
        def groupedFees = service.getAllOptimizedBySince(fees as List<PatentOfficeFee>)

        then: 'result group should be correct'
        groupedFees.values().collect { it.id } == result.values().collect { it.id }

        where:
        fees                                         || result
        null                                         || [:]
        []                                           || [:]
        [fee(1, '2001-01-01'), fee(2, '2001-01-01')] || ['2001-01-01': fees]
        [fee(1, '2001-01-01'), fee(2, '2000-01-01')] || ['2001-01-01': [fee(1, '2001-01-01')],
                                                         '2000-01-01': [fee(2, '2000-01-01')]]
    }

    def fee(Long id, String since) {
        new PatentOfficeFee() {}.with { it.id = id; it.since = java.sql.Date.valueOf(since); it }
    }
}
