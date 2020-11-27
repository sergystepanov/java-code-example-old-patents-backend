package ru.ineureka.patents.legacy.api.v1.dto

import spock.lang.Shared
import spock.lang.Specification

class PaymentCaseDtoSpec extends Specification {

    @Shared
    def object = new PaymentCaseDto('1', '1', '1', '1', '1', '1', '1')

    def "if getters are ok"() {
        expect:
        object.getId() == '1'
        object.getCount() == '1'
        object.getDateCreated() == '1'
        object.getDoc() == '1'
        object.getMaxDueDate() == '1'
        object.getMinDueDate() == '1'
        object.getPartnerId() == '1'
    }

    def "if toString is ok"() {
        expect:
        !object.toString().isBlank()
    }
}
