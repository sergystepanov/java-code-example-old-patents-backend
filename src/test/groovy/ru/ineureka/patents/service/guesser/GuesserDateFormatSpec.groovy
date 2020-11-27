package ru.ineureka.patents.service.guesser

import spock.lang.Specification

class GuesserDateFormatSpec extends Specification {
    def "if toString method is ok"() {
        expect:
        !new GuesserDateFormat("123", null).toString().isBlank()
    }
}
