package ru.ineureka.patents.service.property

import ru.ineureka.patents.documents.converter.Converter
import ru.ineureka.patents.service.PatentOfficeProxy
import ru.ineureka.patents.service.ProxyServerService
import ru.ineureka.patents.service.cache.DataResult
import ru.ineureka.patents.service.cache.PropertyCache
import ru.ineureka.patents.service.cache.PropertyFileCache
import ru.ineureka.patents.service.converter.HtmlConverterService
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Path

class PropertyStorageServiceSpec extends Specification {

    @Shared
    def proxy = Stub(PatentOfficeProxy, constructorArgs: [null, null])

    @Shared
    def cache = Stub(PropertyFileCache, constructorArgs: [10 as int, Path.of('')])

    @Shared
    def converter = new HtmlConverterService(Stub(Converter))

    def "if property data getter returns data in a positive case"() {
        given: 'a perfectly normal service and proxy'
        def stubProxy = Stub(PatentOfficeProxy, constructorArgs: [null as ProxyServerService, null as PropertyCache]) {
            getInput(_ as PropertyRequest) >> new DataResult([0x0] as byte[], true)
        }
        def service = new PropertyStorageService(stubProxy as PatentOfficeProxy, cache as PropertyCache, converter)

        when: 'some data is requested'
        def data = service.getPropertyData(['', '', ''] as PropertyRequest)

        then: 'expected data is returned'
        data.data == [0x0] as byte[]

        and: 'there is no exceptions'
        noExceptionThrown()
    }

    def "if property data getter throws exceptions"() {
        given: 'a broken service'
        def stubProxy = Stub(PatentOfficeProxy, constructorArgs: [null as ProxyServerService, null as PropertyCache]) {
            getInput(_ as PropertyRequest) >> { throw new IOException('test') }
        }
        def service = new PropertyStorageService(stubProxy as PatentOfficeProxy, cache as PropertyCache, converter)

        when: 'some data is requested'
        service.getPropertyData(['', '', ''] as PropertyRequest)

        then: 'there is exception'
        thrown(PropertyStorageException)
    }

    def "GetDocument"() {
    }

    def "GetFile"() {
    }

    def "GetPdf"() {
    }

    def "GetProperty"() {
    }

    def "Cache"() {
    }
}
