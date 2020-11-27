package ru.ineureka.patents.legacy.api.v1.response


import spock.lang.Shared
import spock.lang.Specification

class PayResponseSpec extends Specification {

    @Shared
    def object = new PayResponse(1)

    def "if getters are ok"() {
        expect:
        object.getPaid() == 1
    }

    def "if toString is ok"() {
        expect:
        !object.toString().isBlank()
    }
}
