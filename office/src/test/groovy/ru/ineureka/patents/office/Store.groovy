package ru.ineureka.patents.office

import groovy.json.JsonSlurper
import ru.ineureka.patents.office.mapper.JsonMapper
import ru.ineureka.patents.office.mapper.PropertyMapper

import java.nio.file.Paths
import java.util.function.BiFunction
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class Store<T extends PatentOffice> {
    private ZipFile dataSource
    private String archivePath
    private String jsonPath
    private PropertyMapper jsonMapper
    private BiFunction<String, byte[], T> constructFn

    Store() {
        this.archivePath = ''
        this.jsonPath = ''
        this.jsonMapper = new JsonMapper()
        this.constructFn = { -> }
    }

    def withArchivePath = { path -> this.archivePath = path; this }

    def withJson = { path -> this.jsonPath = path; this }

    def build = { this.dataSource = new ZipFile(getPath(this.archivePath).toString()); this }

    /**
     * A polymorphic document constructor.
     * @param producer
     * @return Some generator function.
     */
    def withDocumentConstructor = { func -> this.constructFn = func; this }

    def getDocument = { String fileName -> constructFn.apply(fileName, readZipEntry(fileName + '.html')) }

    def getPatent = { String fileName -> getDocument(fileName).getPatent() }

    def getJson = { String fileName -> readJson fileName + '.json' }

    def getDocumentAsStream = { String fileName -> readZipEntry(fileName + '.html') }

    static def getPath(String fileName) { Paths.get('src/test/resources/' + fileName).toAbsolutePath() }

    /**
     * Returns specified number of random entry names.
     *
     * @param limit How much entries to return.
     */
    def getRandomListOfDocuments(int limit = 3) {
        def size = this.dataSource.size()
        if (size < 1) []

        def max = Math.min(limit, size)
        def indexes = []
        (1..max).each { indexes << new Random().nextInt(size) }

        List<String> names = []
        def entries = this.dataSource.entries()
        int index = 0
        while (entries.hasMoreElements() && names.size() < max) {
            def entry = (ZipEntry) entries.nextElement()
            def fileName = entry.getName()
            if (!entry.isDirectory() && !fileName.contains('/') && indexes.contains(index)) names.add(fileName)
            index++
        }

        names.collect { it.replace('.html', '') }
    }

    def readZipEntry(String fileName) {
        def stream = new ByteArrayOutputStream()

        new BufferedInputStream(dataSource.getInputStream(dataSource.getEntry(fileName))).withCloseable {
            def buffer = new byte[8192]
            int length
            while ((length = it.read(buffer)) != -1) {
                stream.write(buffer, 0, length)
            }
            stream.flush()
            stream.close()
        }

        return stream.toByteArray()
    }

    def readJson = { String fileName -> new JsonSlurper().parse(getPath(jsonPath + '/' + fileName).toFile()) }
}
