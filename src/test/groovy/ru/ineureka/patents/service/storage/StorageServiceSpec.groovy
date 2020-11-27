package ru.ineureka.patents.service.storage

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class StorageServiceSpec extends Specification {

    @Shared
    def service = new StorageService()

    @Unroll
    def "if hash function is ok for byte #input"() {
        expect:
        service.getHash(new ByteArrayInputStream(input as byte[])) == output

        where:
        input              || output
        [0x10, 0x20, 0x30] || 'b203c5a0c19f15f173698158e08f83ca07638574'
        []                 || 'da39a3ee5e6b4b0d3255bfef95601890afd80709' // it's proper SHA1 for an empty string
    }

    @Unroll
    def "if hash function is ok for string #input"() {
        expect:
        service.getHash(input) == output

        where:
        input || output
        'abc' || 'a9993e364706816aba3e25717850c26c9cd0d89d'
        'абц' || '66313771ebfe6191bcf37f370285f1ad24015a9e'
        ''    || 'da39a3ee5e6b4b0d3255bfef95601890afd80709'
    }
}
