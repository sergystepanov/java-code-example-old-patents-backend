package ru.ineureka.patents.legacy.api.v1.dto

import spock.lang.Shared
import spock.lang.Specification

class PaymentCheckPayRequestDtoSpec extends Specification {

    @Shared
    def object = new PaymentCheckPayRequestDto(['1'] as String[])

    def "if getters are ok"() {
        expect:
        object.getIds() == ['1'] as String[]
    }

    def "if toString is ok"() {
        expect:
        !object.toString().isBlank()
    }
}
