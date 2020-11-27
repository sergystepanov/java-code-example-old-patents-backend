package ru.ineureka.patents.legacy.api.v1.dto


import spock.lang.Specification

class PaymentCheckRecordDtoSpec extends Specification {

    def object = new PaymentCheckRecordDto('1', '1', '1', '1', '1', '1', '1', '1', '1', '1')

    def "if getters are ok"() {
        expect:
        object.getId() == '1'
        object.getAnnuity() == '1'
        object.getAnnuityEnd() == '1'
        object.getApplicationNo() == '1'
        object.getDoc() == '1'
        object.getDueDate() == '1'
        object.getPaid() == '1'
        object.getRegistrationNumber() == '1'
        object.getRegistry() == '1'
        object.getType() == '1'
        !object.isCached()
        !object.isPdf()
    }

    def "if setters are ok"() {
        when:
        object.setCached(true)
        object.setPdf(true)
        object.setPaid('2')

        then:
        object.getPaid() == '2'
        object.isPdf()
        object.isCached()
    }

    def "if toString is ok"() {
        expect:
        !object.toString().isBlank()
    }
}
