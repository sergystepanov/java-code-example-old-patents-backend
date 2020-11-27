package ru.ineureka.patents.office

import com.google.common.flogger.FluentLogger
import ru.ineureka.patents.office.dto.PatentExtendedDto
import ru.ineureka.patents.office.exception.PatentOfficeException
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PatentOfficeSpec extends Specification {

    def "if generic patent office builds itself properly"() {
        when:
        def office = new TestOffice('text'.getBytes(StandardCharsets.UTF_8))

        then:
        office.text == 'text'

        and:
        office.encoding == 'UTF-8'
    }

    def "if some patent office has invalid encoding"() {
        when:
        new InvalidOffice([0] as byte[])

        then:
        thrown(PatentOfficeException)
    }

    @Unroll
    def "if date conversion is ok for date: #date"() {
        given:
        def office = new TestOffice('test'.getBytes())

        expect:
        office.toDateFormat(date) == result

        where:
        date         || result
        null         || null
        '  '         || null
        ''           || null
        '2000-01-01' || LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
    }

    static class TestOffice extends PatentOffice {
        private static final logger = FluentLogger.forEnclosingClass()

        TestOffice(byte[] bytes) throws PatentOfficeException {
            super(bytes)
        }

        @Override
        protected boolean isDocumentAlrightAlrightAlright() throws PatentOfficeException {
            return true
        }

        @Override
        protected void setDocumentDateFormat() {
            documentDateFormat = DateTimeFormatter.ISO_DATE
        }

        @Override
        PatentExtendedDto getPatent() throws PatentOfficeException {
            return null
        }

        @Override
        protected FluentLogger getLogger() {
            return logger
        }
    }

    static class InvalidOffice extends TestOffice {
        InvalidOffice(byte[] bytes) throws PatentOfficeException {
            super(bytes)
        }

        @Override
        protected String getEncoding() {
            return 'invalid'
        }
    }
}
