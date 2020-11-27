package ru.ineureka.patents.service.guesser

import ru.ineureka.patents.office.Office
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static ru.ineureka.patents.service.cases.Fields.*

class PropertyDataGuesserSpec extends Specification {

    @Shared
    def guesser = new PropertyDataGuesser()

    @Unroll
    def "if date guesser works as expected for #input -> #output"() {
        when:
        def date = guesser.getDate(input)

        then:
        date == output

        where:
        input          || output
        ''             || ''
        '2001-01-01'   || '2001-01-01'
        '01-01-01'     || '2001-01-01'
        '01.01.2001'   || '2001-01-01'
        '01 Jan 2001'  || '2001-01-01'
        '1st Jan 2001' || '2001-01-01'
        '01-Jan-01'    || '2001-01-01'
        '1/1/01'       || '2001-01-01'
        '2001 Jan 21'  || '2001 Jan 21'
    }

    @Unroll
    def "if nation to registry converter works as expected for #input -> #output"() {
        expect:
        guesser.getRegistry(input) == output

        where:
        input                            || output
        [:]                              || Office.FIPS
        [nation: 'eurasia (ea)']         || Office.EAPO
        [nation: 'eurasian']             || Office.EAPO
        [description: 'eurasian patent'] || Office.EAPO
    }

    @Unroll
    def "if annuity periods are guessed properly -> #annuities"() {
        when:
        def fields = data.collectEntries { k, v -> [(k.toString()): v] } as Map<String, String>

        then:
        guesser.getAnnuityPeriod(fields) == annuities as String[]

        where:
        data                                                                        || annuities
        [(TYPE): 'd', (PAYMENT_ANNUITY): '6', (APPLICATION_DATE): '2015-01-01']     || [6, 10]
        [(TYPE): 'd', (PAYMENT_ANNUITY): '6,7', (APPLICATION_DATE): '2015-01-01']   || [6, 10]
        [(TYPE): 'd', (PAYMENT_ANNUITY): '5', (APPLICATION_DATE): '2015-01-01']     || [5, 5]
        [(TYPE): 'd', (PAYMENT_ANNUITY): '22', (APPLICATION_DATE): '2011-01-01']    || [22, 22]
        [(TYPE): 'd', (PAYMENT_ANNUITY): '11-15', (APPLICATION_DATE): '2015-01-01'] || [11, 15]
        [(TYPE): 'p', (PAYMENT_ANNUITY): '22', (APPLICATION_DATE): '2018-01-01']    || [22, 22]
        [(TYPE): 'd', (PAYMENT_ANNUITY): '4d-5g', (APPLICATION_DATE): '2018-01-01'] || [4, 5]
    }

    @Unroll
    def "if convert with various formats just works for #input"() {
        expect:
        guesser.getDate(input) == output

        where:
        input          || output
        '2nd Jun 2003' || '2003-06-02'
        '1999-03-02'   || '1999-03-02'
    }

    @Unroll
    def "if grace period guesser is correct for #input -> #output"() {
        expect:
        guesser.getGracePeriod(input) == output

        where:
        input                                 || output
        [:]                                   || ""
        [(DUE_DATE.toString()): '']           || ""
        [(DUE_DATE.toString()): 'garbage']    || ""
        [(DUE_DATE.toString()): '2001-01-01'] || '2001-07-01'
    }

    @Unroll
    def "if expiry date guesser is correct for #input -> #output"() {
        expect:
        guesser.getExpiryDate(input, 1) == output

        where:
        input                                         || output
        [:]                                           || ""
        [(APPLICATION_DATE.toString()): '']           || ""
        [(APPLICATION_DATE.toString()): 'garbage']    || ""
        [(APPLICATION_DATE.toString()): '2001-01-01'] || '2002-01-01'
    }
}
