package ru.ineureka.patents.office.org.eapo

import ru.ineureka.patents.office.Store
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class EapoSpec extends Specification {

    @Shared
    def store = new Store<Eapo>()
            .withArchivePath('eapo/html/eapo_v1_archive.zip')
            .withDocumentConstructor({ _, data -> new Eapo(data) })
            .build()

    def "if a basic parser is correct"() {
        when:
        def doc = store.getDocument('001234').getPatent()

        then:
        !doc.getProprietors().isEmpty()
    }

    @Unroll
    def "if corrections are parsed correctly for #fileName"() {
        when:
        def doc = store.getDocument(fileName).getPatent()

        then:
        doc.isCorrections() == hasCorrections
        doc.getCorrections_text() == text

        where:
        fileName | hasCorrections | text
        '026455' | false          | ''
        '06351'  | true           | '''<table class=c border=0 cellpadding=5 cellspacing=0 width=100%>
<tr><td class=c><p align='right'>Сведения о передаче права на патент путем уступки права</td><td class=c>
В качестве патентовладельца зарегистрирован(а, о, ы) <b>ТЕНАРИС КОННЕКШНС Б.В. (NL)</b>.<br>
Дата регистрации передачи права путём уступки <b>2016.08.16</b>, свидетельство <b>2759/1У-006351</b>.<br>
Публикация в бюллетене № <b><a href="/ru/publications/bulletin/ea201701/PC4A.html" target="_blank">01</a></b> за <b>2017</b> год.</td></tr>
<tr><td class=c><p align='right'>Сведения об изменении имени или наименования патентовладельца</td><td class=c>
Новое наименование патентовладельца <b>ТЕНАРИС КОННЕКШНС ЛИМИТЕД   (VC)</b>.<br>
Дата регистрации <b>2010.05.11</b><br>
Публикация в бюллетене № <b><a href="/ru/publications/bulletin/ea201003/TC4A.html" target="_blank">03</a></b> за <b>2010</b> год.</td></tr>
</table>'''
    }

    def "if latest annuities are parsed correctly"() {
        expect:
        store.getDocument('026455').getPatent().getAnnuity() == '6'
    }
}
