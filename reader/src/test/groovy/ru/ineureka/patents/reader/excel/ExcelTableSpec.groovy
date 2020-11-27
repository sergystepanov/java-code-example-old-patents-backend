package ru.ineureka.patents.reader.excel

import spock.lang.Specification
import spock.lang.Unroll

class ExcelTableSpec extends Specification {

    @Unroll
    def "if data getter works for #input"() {
        expect:
        new ExcelTable(input).getData() == output

        where:
        input                                      || output
        [:]                                        || [:]
        [0: [0: 'a', 1: 'b']]                      || [:]
        [0: [0: 'a', 1: 'b'], 1: [0: 'c', 1: 'd']] || [1: [0: 'c', 1: 'd']]
    }

    @Unroll
    def "if normalized header is correct for #input"() {
        expect:
        new ExcelTable(input).getNormalizedHeader() == output

        where:
        input                                 || output
        [:]                                   || [:]
        [1: [1: 'ColUmN \t\n\r1', 2: '子ぅ 2']] || [1: 'column_1', 2: '2']
    }

    @Unroll
    def "if warnings work ok for #warnings"() {
        given:
        def table = new ExcelTable([:])

        when:
        table.addWarning(warnings as String[])

        then:
        table.getWarnings() == warnings

        where:
        warnings        || output
        []              || []
        ['a', 'b', 'c'] || warnings
    }

    def "if toString is ok"() {
        expect:
        !new ExcelTable([:]).toString().isEmpty()
    }
}
