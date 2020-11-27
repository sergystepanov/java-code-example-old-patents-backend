package ru.ineureka.patents.documents.converter

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Path
import java.nio.file.Paths

class HtmlToPdfConverterSpec extends Specification {

    @Shared
    def converter = new HtmlToPdfConverter()

    def cleanupSpec() {
        converter.shutdown()
    }

    @Unroll
    def "if converter is ok in positive cases"() {
        when:
        def result = converter.convert getPath(input)

        then:
        !result.isBlank()

        where:
        input            || _
        'html/test.html' || _
    }

    @Unroll
    def "if converter is not ok in negative cases"() {
        when:
        converter.convert input

        then:
        thrown ConverterException

        where:
        input                || _
        null                 || _
        Path.of('./garbage') || _
    }

    def getPath(String fileName) {
        return Paths.get("src/test/resources/" + fileName)
    }
}
