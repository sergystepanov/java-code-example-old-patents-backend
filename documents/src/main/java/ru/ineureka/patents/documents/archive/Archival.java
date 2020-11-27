package ru.ineureka.patents.documents.archive;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface Archival {

    /**
     * Generates new archive.
     *
     * @param fileList    A list of files to compress.
     * @param destination A destination path of the archive.
     * @return Archive file.
     * @throws ArchivalException In case of creation errors.
     */
    File make(List<Path> fileList, Path destination) throws ArchivalException;
}
