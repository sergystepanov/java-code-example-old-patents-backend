package ru.ineureka.patents.documents.archive;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class Compressor {
    private final boolean useRandomName;

    public Compressor(boolean useRandomName) {
        this.useRandomName = useRandomName;
    }

    public File compress(ArchiveType type, List<Path> fileList, Path destination) throws ArchivalException {
        Archival archival;
        switch (type) {
            case GZ:
                archival = new GzArchival();
                break;
            case ZIP:
            default:
                archival = new ZipArchival();
        }

        final var ext = type.toExtension();
        final var outputFilePath = useRandomName ?
                destination.resolve(getRandomName() + ext) : Path.of(destination.toAbsolutePath().toString() + ext);

        return archival.make(fileList, outputFilePath);
    }

    private static String getRandomName() {
        return UUID.randomUUID().toString();
    }
}
