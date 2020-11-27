package ru.ineureka.patents.service.cache

import ru.ineureka.patents.documents.FileType
import ru.ineureka.patents.service.property.PropertyRequest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files
import java.nio.file.Path

class PropertyFileCacheSpec extends Specification {

    @Shared
    def testPath = Path.of(System.getProperty('java.io.tmpdir'))

    def "if initialization is ok"() {
        given: 'a new file cache service'
        def path = testPath.resolve('f_cache_test')
        def service = new PropertyFileCache(100, path)

        expect: 'the service is created'
        service != null

        and: 'cache dir was created as well'
        Files.exists(path)

        cleanup:
        path.deleteDir()
    }

    def "if initialization works for existing dir"() {
        given: 'an existing cache dir'
        def path = testPath.resolve('f_cache_test')
        Files.createDirectory(path)

        when: 'create a new file cache service'
        def service = new PropertyFileCache(100, path)

        then: 'the service is created'
        service != null

        cleanup:
        path.deleteDir()
    }

    def "if cache put is working"() {
        given: 'cache service'
        def service = new PropertyFileCache(100, testPath)

        and: 'a property'
        def property = new PropertyRequest('test', 'p', '0123')

        when: 'some property is cached'
        service.put(property, [0x0] as byte[])

        and: 'this property is requested from the cache'
        def file = service.get(property).get()

        then: 'the property is cached for sure'
        file.getBytes() == [0x0] as byte[]

        cleanup:
        file.delete()
    }

    def "if cache put is throwing exceptions"() {
        given: 'cache service'
        def service = new PropertyFileCache(100, testPath)

        and: 'a property'
        def property = new PropertyRequest('test', 'p', '0123')

        when: 'some property is cached'
        service.put(property, [0x0] as byte[])

        and: 'this property is cached again'
        def file = service.get(property).get().with { it.setReadOnly(); it }
        service.put(property, [0x1] as byte[])

        then: 'the property is not re-cached due to error'
        file.getBytes() == [0x0] as byte[]

        cleanup:
        file.delete()
    }

    def "if cache remove is ok"() {
        given: 'cache service'
        def service = new PropertyFileCache(100, testPath)

        and: 'a property'
        def property = new PropertyRequest('test', 'p', '0124')

        when: 'some property is cached'
        service.put(property, [0x0] as byte[])

        and: 'this property is requested from the cache'
        def file = service.get(property).get()

        and: 'this property is removed'
        service.remove([property])

        and: 'requested from the cache'
        def propertyFile = service.get(property)

        then: 'the property is cached for sure'
        propertyFile.isEmpty()

        cleanup:
        file.delete()
    }

    @Unroll
    def "if property cache status is ok for #propertyGet -> #cached"() {
        given: 'cache service'
        def service = new PropertyFileCache(1000, testPath)

        and: 'a property'
        def property = new PropertyRequest('test', 'p', '0123')

        when: 'some property is cached'
        service.put(property, [0x0] as byte[])

        then: 'the property is cached for sure'
        service.isCached(propertyGet) == cached

        cleanup:
        service.remove([property])

        where:
        propertyGet                              || cached
        new PropertyRequest('test', 'p', '0123') || true
        new PropertyRequest('test', 'p', '0125') || false
    }

    def "if cache honors cache time property"() {
        given: 'cache service'
        def service = new PropertyFileCache(100, testPath)

        and: 'a property'
        def property = new PropertyRequest('test', 'p', '0123' + wait)

        when: 'some property is cached'
        service.put(property, [0x0] as byte[])

        then: 'wait for cache some'
        sleep(wait)

        and: 'the property is cached or not for sure'
        service.isCached(property) == cached

        cleanup:
        service.remove([property])

        where:
        wait || cached
        100  || false
        1    || true
    }

    def "if system path for cache is ok"() {
        given: 'an existing cache dir'
        def path = testPath.resolve('f_cache_test')
        Files.createDirectory(path)

        when: 'create a new file cache service'
        def service = new PropertyFileCache(100, path)

        then: 'the service is created'
        service != null

        and: 'the system path is what init was'
        service.getSystemPath() == path

        cleanup:
        Files.delete(path)
    }

    def "if cache returns various file types"() {
        given: 'an existing cache dir'
        def service = new PropertyFileCache(100, testPath)

        and: 'a property'
        def property = new PropertyRequest('test', 'p', '0123')

        when:
        service.put(property, [0x0] as byte[])
        def html = service.getFilePath(property, FileType.HTML)
        def pdf = service.getFilePath(property, FileType.PDF)

        then:
        html.getFileName().toString().endsWith('html')
        html.toFile().exists()

        and:
        pdf.getFileName().toString().endsWith('pdf')
        !pdf.toFile().exists()

        cleanup:
        Files.delete(html)
    }
}
