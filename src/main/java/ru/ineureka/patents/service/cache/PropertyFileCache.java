package ru.ineureka.patents.service.cache;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.documents.FileType;
import ru.ineureka.patents.service.property.PropertyRequest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * A file based implementation of the properties cache.
 * Not thread safe.
 */
@Service
public class PropertyFileCache implements PropertyCache {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final int time;
    private final Path cachePath;

    public PropertyFileCache(@Value("${app.service.cache.time}") int time,
                             @Value("${app.service.cache.path}") Path cachePath) {
        this.time = time;
        this.cachePath = cachePath;

        if (!Files.exists(this.cachePath)) {
            try {
                Files.createDirectories(this.cachePath);
            } catch (Exception e) {
                logger.atSevere().withStackTrace(StackSize.SMALL)
                        .withCause(e)
                        .log("Couldn't create the path: %s", cachePath);
                throw new RuntimeException("Cache path creation failed");
            }
            logger.atInfo().log("Creating the save path %s", cachePath);
        }
    }

    /**
     * @see PropertyCache#get(PropertyRequest)
     * @since 3.0.0
     */
    @Override
    public Optional<File> get(PropertyRequest request) {
        return isCached(request) ? Optional.of(toProperty(request).toFile()) : Optional.empty();
    }

    /**
     * @see PropertyCache#put(PropertyRequest, byte[])
     * @since 3.0.0
     */
    @Override
    public void put(PropertyRequest request, byte[] bytes) {
        try {
            Files.write(toProperty(request), bytes);
            logger.atFine().log("The file %s has been saved", request);
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("The error during save: %s", request);
        }
    }

    @Override
    public void remove(List<PropertyRequest> requests) {
        for (var request : requests) {
            final var path = toProperty(request);

            if (path.toFile().exists()) {
                try {
                    Files.delete(path);
                    logger.atFine().log("The file for %s has been removed", request);
                } catch (Exception e) {
                    logger.atSevere().withCause(e).withStackTrace(StackSize.SMALL).log("File delete error");
                }
            }
        }
    }

    /**
     * @see PropertyCache#isCached(PropertyRequest)
     * @since 3.0.0
     */
    @Override
    public boolean isCached(PropertyRequest request) {
        final var file = toProperty(request).toFile();

        if (!file.exists()) return false;

        final var fileTime = file.lastModified();
        final long diff = Instant.now().toEpochMilli() - (fileTime > 0 ? fileTime : 0);
        logger.atFine().log("Property age is: %s ms of %s", diff, time);

        return diff <= time;
    }

    @Override
    public Path getSystemPath() {
        return cachePath;
    }

    /**
     * Returns a full path to the cached file.
     *
     * @param request The data object for the name and path generation.
     * @since 3.0.0
     */
    private Path toProperty(PropertyRequest request) {
        return cachePath.resolve(request.getFileName() + "." + FileType.HTML.name().toLowerCase());
    }

    /**
     * Returns the path to the base property with specified file type.
     *
     * @param request A property request to find.
     * @param type    The file type to search for.
     */
    public Path getFilePath(PropertyRequest request, FileType type) {
        final var propertyPath = toProperty(request);

        final Path resultPath = propertyPath
                .getParent()
                .resolve(request.getFileName() + "." + type.get().toLowerCase());
        logger.atFine().log("Result path: %s", resultPath);

        return resultPath;
    }
}
