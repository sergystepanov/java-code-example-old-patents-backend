package ru.ineureka.patents.reader.excel


import ru.ineureka.patents.reader.excel.exception.ExcelReaderException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Paths

import static ru.ineureka.patents.reader.excel.exception.ExcelReaderExceptionType.AN_ERROR
import static ru.ineureka.patents.reader.excel.exception.ExcelReaderExceptionType.TOO_OLD

class ExcelReaderSpec extends Specification {

    @Shared
    def reader = new ExcelReader()

    def "if hidden rows and cols are found"() {
        given:
        def fileStream = stream file

        when:
        def table = reader.read(fileStream)

        then:
        table != null

        and: "has hidden columns"
        table.getWarnings()[0].contains(': 4')

        and: "has hidden rows"
        table.getWarnings()[1].contains(': 5')

        cleanup:
        fileStream.close()

        where:
        file = 'excel/test-hide-001.xlsx'
    }

    @Unroll
    def "if dates a read correctly in #fileName"() {
        given:
        def fileStream = stream fileName

        when:
        def table = reader.read(fileStream)

        then:
        table != null

        and: "dates are correct"
        table.getData().values().collect { it.get(2) } == result

        cleanup:
        fileStream.close()

        where:
        fileName                    || result
        'excel/test-dates-003.xlsx' || ['1978-11-21', '1968-01-01', '1999-11-11']
    }

    def "if not initialized cells in the doc are skipped"() {
        setup:
        def file = stream 'excel/test-not-initialized-cells.xlsx'

        expect:
        reader.read(file).getData() == [4: [0: '1', 1: '2', 2: '3'], 24: [0: '1', 1: '2'], 30: [0: '1']]

        cleanup:
        file.close()
    }

    @Unroll
    def "if reader throws proper exceptions for #fileName -> #exception"() {
        given:
        def file = stream fileName

        when:
        reader.read file

        then:
        def ex = thrown ExcelReaderException
        ex.type == exception

        and:
        !ex.getMessage().isBlank() == hasMessage

        cleanup:
        file.close()

        where:
        fileName                        || exception | hasMessage
        'excel/test-old-format-001.xls' || TOO_OLD   | false
        'garbage.file'                  || AN_ERROR  | true
    }

    def stream(String fileName) { Paths.get('src/test/resources/' + fileName).toFile().newInputStream() }
}
