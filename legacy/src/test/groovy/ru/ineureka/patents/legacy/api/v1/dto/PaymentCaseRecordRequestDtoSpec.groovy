package ru.ineureka.patents.legacy.api.v1.dto

import spock.lang.Shared
import spock.lang.Specification

class PaymentCaseRecordRequestDtoSpec extends Specification {

    @Shared
    def object = new PaymentCaseRecordRequestDto('1', '1', '1', '1')

    def "if getters are ok"() {
        expect:
        object.getNumber() == '1'
        object.getOffice() == '1'
        object.getType() == '1'
        object.getYears() == '1'
    }

    def "if toString is ok"() {
        expect:
        !object.toString().isBlank()
    }
}
