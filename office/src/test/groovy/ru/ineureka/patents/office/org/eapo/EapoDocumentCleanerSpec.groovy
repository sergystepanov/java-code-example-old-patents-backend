package ru.ineureka.patents.office.org.eapo

import ru.ineureka.patents.office.Store
import spock.lang.Shared
import spock.lang.Specification

class EapoDocumentCleanerSpec extends Specification {

    @Shared
    def store = new Store<Eapo>()
            .withArchivePath('eapo/html/eapo_v1_archive.zip')
            .withDocumentConstructor({ _, data -> new Eapo(data) })
            .build()

    def "if cleaning is working"() {
        when:
        def cleaned = new String(EapoDocumentCleaner.clean(store.getDocumentAsStream('001674')), Eapo.ENCODING)

        then: 'there is no script'
        !cleaned.contains('<script')

        and: 'images have absolute url'
        cleaned.contains(Eapo.URL + '/images/')

        and: 'styles have absolute url'
        cleaned.contains(Eapo.URL + '/css/')
    }
}
