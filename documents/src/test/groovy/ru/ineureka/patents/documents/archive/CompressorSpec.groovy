package ru.ineureka.patents.documents.archive

import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Path
import java.util.zip.ZipFile

import static ru.ineureka.patents.documents.archive.ArchiveType.GZ
import static ru.ineureka.patents.documents.archive.ArchiveType.ZIP

class CompressorSpec extends Specification {

    static Path testPath = Path.of System.getProperty('java.io.tmpdir')

    @Unroll
    def "if #compression compressor is alright"() {
        given: 'a compressor'
        def compressor = new Compressor(true)

        and: 'a list of random files'
        def files = getSomeRandomFiles(3) as List<Path>
        def inputFileNames = files.collect { it.fileName.toString() } as String[]

        and: 'some files which do not exist'
        files = files + ['a', 'b', 'c'].collect { Path.of(it) }

        when: 'the list of files was compressed'
        def archive = compressor.compress(compression, files, testPath)
        archive.deleteOnExit()

        and: 'a given list of files was returned'
        def outputFileNames = []
        if (compression == GZ) {
            outputFileNames = readFilenameFromTarArchive archive
        } else {
            if (compression == ZIP) {
                outputFileNames = new ZipFile(archive as File).withCloseable { it.entries().toList() } as String[]
            }
        }

        then: 'the archive ends with a proper extension'
        archive.name.endsWith extension

        and: 'input file list equals to the list of files inside the archive'
        inputFileNames == outputFileNames

        where:
        compression | extension || _
        GZ          | '.gz'     || _
        ZIP         | '.zip'    || _
    }

    @Unroll
    def "if #compression compressor throws errors"() {
        given: 'a compressor'
        def compressor = new Compressor(false)

        and: 'a list of random files'
        def files = getSomeRandomFiles(1)

        and: 'result archive name'
        def archiveName = testPath.resolve 'file'

        when: 'the list of files was compressed'
        def archive = compressor.compress(compression, files, archiveName)
        archive.deleteOnExit()

        and: 'archive is locked'
        archive.setReadOnly()

        and: 'second call for same archive is executed'
        compressor.compress(compression, files, archiveName)

        then: 'exception should be thrown'
        thrown ArchivalException

        where:
        compression || _
        GZ          || _
        ZIP         || _
    }

    def getSomeRandomFiles(number) {
        (1..number).collect {
            def file = File.createTempFile(it + "temp", ".tmp")
            file.deleteOnExit()
            file.write UUID.randomUUID().toString()
            file.toPath()
        }
    }

    def readFilenameFromTarArchive(file) {
        def names = []
        TarArchiveEntry entry
        new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(file))).withCloseable {
            while ((entry = it.nextTarEntry) != null) {
                names << entry.name
            }
        }
        names
    }
}
