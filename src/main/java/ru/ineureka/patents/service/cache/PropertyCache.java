package ru.ineureka.patents.service.cache;

import ru.ineureka.patents.documents.FileType;
import ru.ineureka.patents.service.property.PropertyRequest;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface PropertyCache {

    Optional<File> get(PropertyRequest request);

    void put(PropertyRequest request, byte[] bytes);

    void remove(List<PropertyRequest> requests);

    boolean isCached(PropertyRequest request);

    Path getFilePath(PropertyRequest request, FileType fileType);

    Path getSystemPath();
}
