package ru.ineureka.patents.office.org.fips

import ru.ineureka.patents.office.Store
import spock.lang.Shared
import spock.lang.Specification

class FipsDocumentCleanerSpec extends Specification {

    @Shared
    def store = new Store<Fips>()
            .withArchivePath('fips/html/v2/fips_v2_archive.zip')
            .withDocumentConstructor({ fileName, data -> new Fips(fileName.substring(0, 1), data) })
            .build()

    def "if cleaning is working"() {
        when:
        def cleaned = new String(FipsDocumentCleaner.clean(store.getDocumentAsStream('p2201459')), Fips.ENCODING)

        then:
        !cleaned.contains('<script')
        !cleaned.contains('<noscript')
        !cleaned.contains('RFP_LOGO.gif')
        !cleaned.contains('http://www.fips.ru/cdfi/Fonts/WipoUni.ttf')
    }

    def "if cleaning is ok with empty docs"() {
        given: 'an empty input data to clean'
        def dat = [] as byte[]

        when: 'the empty doc was cleaned'
        def cleaned = new String(FipsDocumentCleaner.clean(dat))

        then: 'the result is empty HTML document'
        cleaned == '<html><head></head><body></body></html>'
    }

    def "if cleaning will fail on malformed input data"() {
        expect:
        FipsDocumentCleaner.clean(null) == null
    }
}
