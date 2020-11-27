package ru.ineureka.patents.documents.archive;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class GzArchival implements Archival {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public GzArchival() {
    }

    @Override
    public File make(List<Path> fileList, Path destination) throws ArchivalException {
        final var outputFile = destination.toFile();

        try (var out = new TarArchiveOutputStream(
                new GzipCompressorOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(outputFile))))) {
            for (var file : fileList) {
                add(out, file);
            }

            return outputFile;
        } catch (IOException e) {
            logger.atSevere().withCause(e).withStackTrace(StackSize.SMALL).log(e.getMessage());
            throw new ArchivalException();
        }
    }

    private static void add(TarArchiveOutputStream output, Path path) throws IOException {
        var file = path.toFile();

        if (file.exists()) {
            TarArchiveEntry tarEntry = new TarArchiveEntry(file, file.getName());
            output.putArchiveEntry(tarEntry);

            try (var fileStream = new FileInputStream(file)) {
                IOUtils.copy(fileStream, output);
            }
            output.closeArchiveEntry();
        }
    }
}
