package ru.ineureka.patents.util

import spock.lang.Specification
import spock.lang.Unroll

class StringUtilsSpec extends Specification {

    @Unroll
    def "if isNotNumber is correct"() {
        when:
        def found = StringUtils.notNumeric.matcher(value).replaceAll('')

        then:
        found == result

        and:
        noExceptionThrown()

        where:
        value || result
        ''    || ''
        '123' || '123'
        'abc' || ''
    }

    def "if isNotNumber handles invalid input"() {
        when:
        StringUtils.notNumeric.matcher(null).matches()

        then:
        thrown(NullPointerException)
    }
}
