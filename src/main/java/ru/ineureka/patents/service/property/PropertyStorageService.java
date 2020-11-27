package ru.ineureka.patents.service.property;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.documents.FileType;
import ru.ineureka.patents.documents.converter.ConverterException;
import ru.ineureka.patents.office.dto.PatentExtendedDto;
import ru.ineureka.patents.office.exception.PatentOfficeException;
import ru.ineureka.patents.office.org.eapo.Eapo;
import ru.ineureka.patents.office.org.eapo.EapoDocumentCleaner;
import ru.ineureka.patents.office.org.eapo.EapoPublicationNumberSearch;
import ru.ineureka.patents.office.org.fips.Fips;
import ru.ineureka.patents.office.org.fips.FipsDocumentCleaner;
import ru.ineureka.patents.service.PatentOfficeProxy;
import ru.ineureka.patents.service.cache.DataResult;
import ru.ineureka.patents.service.cache.PropertyCache;
import ru.ineureka.patents.service.converter.HtmlConverterService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static ru.ineureka.patents.office.Office.*;

@Service
public class PropertyStorageService {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final PatentOfficeProxy patentOfficeProxy;
    private final PropertyCache propertyCache;
    private final HtmlConverterService converter;

    public PropertyStorageService(PatentOfficeProxy patentOfficeProxy, PropertyCache propertyCache,
                                  HtmlConverterService converter) {
        this.patentOfficeProxy = patentOfficeProxy;
        this.propertyCache = propertyCache;
        this.converter = converter;
    }

    public DataResult getPropertyData(PropertyRequest request) throws PropertyStorageException {
        try {
            return patentOfficeProxy.getInput(request);
        } catch (IOException e) {
            throw new PropertyStorageException("Couldn't get the document");
        }
    }

    public DataResult getDocument(PropertyRequest request) throws PropertyStorageException {
        return cache(request, getPropertyData(request));
    }

    public Optional<File> getFile(PropertyRequest request) throws PropertyStorageException {
        var file = propertyCache.get(request);

        if (file.isEmpty()) {
            getDocument(request);
            file = propertyCache.get(request);
        }

        return file;
    }

    public Optional<File> getPdf(PropertyRequest request, boolean force) throws PropertyStorageException {
        var pdfPath = propertyCache.getFilePath(request, FileType.PDF);
        var pdfFile = pdfPath.toFile();

        if (!pdfFile.exists() || force) {
            final var file = getFile(request);
            if (file.isPresent()) {
                try {
                    converter.convertHtml(file.get().toPath(), pdfPath);
                } catch (ConverterException e) {
                    throw new PropertyStorageException("Couldn't convert the file");
                }
            }
        }

        return Optional.of(pdfFile);
    }

    // TODO CUT registry_fee in half if it's open license
    public PatentExtendedDto getProperty(PropertyRequest request) throws PropertyStorageException, PropertyNotFoundException {
        var currentRequest = request;
        final var document = getDocument(request);

        PatentExtendedDto p;
        try {
            switch (request.getOffice()) {
                case FIPS:
                    p = new Fips(request.getType(), document.data).getPatent();
                    // Make a second request for publication data
                    if (p.isProcessPublication() && !p.getGrant_no().isEmpty()) {
                        currentRequest = new PropertyRequest(
                                request.getOffice(), request.getType(), p.getGrant_no(), request.getYears());
                        p = new Fips(request.getType(), getDocument(currentRequest).data).getPatent();
                    }
                    break;
                case EAPO:
                    p = new Eapo(document.data).getPatent();

                    // Make a second request for publication data
                    if (p.getGrant_no().isEmpty()) {
                        final DataResult eapoDocument = getDocument(
                                new PropertyRequest(EAPO_SEARCH, request.getOffice(), request.getNumber(), request.getYears()));
                        final Eapo office = new Eapo(eapoDocument.data);

                        Optional<String> publication = EapoPublicationNumberSearch.getNumber(office.getText());
                        if (publication.isPresent()) {
                            currentRequest = new PropertyRequest(EAPO, request.getType(), publication.get(), request.getYears());
                            p = new Eapo(getDocument(currentRequest).data).getPatent();
                        }
                    }
                    break;
                default:
                    throw new PropertyNotFoundException();
            }
        } catch (PatentOfficeException poe) {
            propertyCache.remove(currentRequest != request ? List.of(request, currentRequest) : List.of(request));
            throw new PropertyStorageException("Couldn't process the request");
        }

        return p;
    }

    public DataResult cache(PropertyRequest request, DataResult document) {
        if (document.isCached) return document;

        byte[] dat;
        switch (request.getOffice()) {
            case FIPS:
                dat = FipsDocumentCleaner.clean(document.data);
                break;
            case EAPO:
            case EAPO_SEARCH:
                dat = EapoDocumentCleaner.clean(document.data);
                break;
            default:
                dat = document.data;
        }

        patentOfficeProxy.save(request, dat);
        logger.atFine().log("Cache property for request: %s", request);

        return document;
    }

    public Optional<InputStream> getResource(String path) throws PropertyStorageException {
        final var propertyFile = propertyCache.getSystemPath().resolve(path).toFile();

        if (!propertyFile.exists()) return Optional.empty();

        try {
            return Optional.of(new FileInputStream(propertyFile));
        } catch (Exception e) {
            logger.atSevere().withCause(e).withStackTrace(StackSize.SMALL).log("Property getter error");
            throw new PropertyStorageException();
        }
    }
}
