package ru.ineureka.patents.documents.archive;

import com.google.common.flogger.FluentLogger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchival implements Archival {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public ZipArchival() {
    }

    @Override
    public File make(List<Path> fileList, Path destination) throws ArchivalException {
        try (var zipData = new ByteArrayOutputStream()) {
            var zos = new ZipOutputStream(zipData);
            for (var filePath : fileList) {
                final var file = filePath.toFile();
                if (file.exists()) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    zos.write(Files.readAllBytes(filePath));
                    zos.closeEntry();
                }
            }
            zos.close();
            Files.write(destination, zipData.toByteArray());

            return destination.toFile();
        } catch (IOException e) {
            logger.atSevere().withCause(e).log("Error during ZIP creation");
            throw new ArchivalException();
        }
    }
}
