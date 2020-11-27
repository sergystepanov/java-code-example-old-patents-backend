package ru.ineureka.patents.documents.generator.office

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Paths
import java.util.concurrent.CountDownLatch

class OfficeDocumentGeneratorSpec extends Specification {

    @Shared
    def generator = new OfficeDocumentGenerator()

    def "if generator works at all"() {
        given: 'a file template to build output document'
        def input = getPath('office/test001.tpl.docx').toFile()
        def output = new ByteArrayOutputStream()

        when: 'generator is called with some replacement params'
        generator.generate(input.newInputStream(), output, ['word': 'test'])

        then: 'output was generated'
        output.toByteArray().length > 0
    }

    @Unroll
    def "if exceptions are created during doc generation with tpl: #template"() {
        when: 'the generator tries to read a broken tpl or write to a bad file'
        generator.generate(getPath(template).newInputStream(), output, [:])

        then: 'an exception should be raised'
        thrown(GeneratorException)

        cleanup:
        output.close()

        where:
        template                  | output
        'test.file'               | new ByteArrayOutputStream()
        'office/test001.tpl.docx' | getAFile().with { it.deleteOnExit(); it }.withDataOutputStream { it.close(); it }
    }

    def "if generator correct with multiple threads"() {
        given: 'a bunch of threads'
        def threads = 21
        def latch = new CountDownLatch(threads)

        and: 'a Word template to process with its result'
        def input = getPath('office/test001.tpl.docx').toFile()
        def output = new ByteArrayOutputStream()

        when: 'a generator is being called in multiple threads concurrently'
        (1..threads).each { new Thread({ generator.generate(input.newInputStream(), output, [:]); latch.countDown() }).run() }
        latch.await()

        then: 'not an exception was thrown'
        noExceptionThrown()
    }

    def "if generator handles incorrect input params correctly"() {
        when:
        generator.generate(input, output, [:])

        then:
        thrown(GeneratorException)

        where:
        input                      | output
        null                       | null
        null                       | new ByteArrayOutputStream()
        new ByteArrayInputStream() | null
    }

    def getPath(String fileName) { Paths.get("src/test/resources/" + fileName) }

    def getAFile() { File.createTempFile("temp", ".tmp") }
}
