package ru.ineureka.patents.controllers;

import com.google.common.flogger.FluentLogger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ineureka.patents.documents.FileType;
import ru.ineureka.patents.documents.archive.ArchivalException;
import ru.ineureka.patents.documents.archive.ArchiveType;
import ru.ineureka.patents.documents.archive.Compressor;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCaseRecordRequestDto;
import ru.ineureka.patents.office.dto.PatentExtendedDto;
import ru.ineureka.patents.payload.FileResponse;
import ru.ineureka.patents.service.cache.DataResult;
import ru.ineureka.patents.service.cache.PropertyCache;
import ru.ineureka.patents.service.property.PropertyNotFoundException;
import ru.ineureka.patents.service.property.PropertyRequest;
import ru.ineureka.patents.service.property.PropertyStorageException;
import ru.ineureka.patents.service.property.PropertyStorageService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/office")
public class RegistryController {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final PropertyCache propertyCache;
    private final PropertyStorageService propertyStorageService;

    public RegistryController(PropertyCache propertyCache, PropertyStorageService propertyStorageService) {
        this.propertyCache = propertyCache;
        this.propertyStorageService = propertyStorageService;
    }

    @GetMapping("/json")
    public PatentExtendedDto getPatentJson(@RequestParam("registry") String registry,
                                           @RequestParam("type") String type,
                                           @RequestParam("number") String number,
                                           @RequestParam(value = "years", required = false, defaultValue = "") String years)
            throws PropertyStorageException, PropertyNotFoundException {
        logger.atFine().log("Request json -> %s %s %s", registry, type, number);

        return propertyStorageService.getProperty(new PropertyRequest(registry, type, number, years));
    }

    @GetMapping("/html")
    public ResponseEntity<Object> getPatentHtml(@RequestParam("registry") String registry,
                                                @RequestParam("type") String type,
                                                @RequestParam("number") String number) throws PropertyStorageException {
        final DataResult document = propertyStorageService.getDocument(new PropertyRequest(registry, type, number));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html; charset=Windows-1251")
                .body(document.data);
    }

    @GetMapping("/pdf")
    public ResponseEntity<Object> getPropertyPdf(@RequestParam("registry") String registry,
                                                 @RequestParam(value = "type", defaultValue = "p") String type,
                                                 @RequestParam("number") String number,
                                                 @RequestParam(value = "years", required = false, defaultValue = "") String years,
                                                 @RequestParam(value = "file-less", defaultValue = "false") boolean fileLess,
                                                 @RequestParam(value = "reload", defaultValue = "false") boolean reload)
            throws PropertyStorageException {
        var propertyRequest = new PropertyRequest(registry, type, number, years);

        var pdf = propertyStorageService.getPdf(propertyRequest, reload);
        if (pdf.isEmpty()) return ResponseEntity.notFound().build();

        if (reload) {
            logger.atInfo().log("Reloading the file for %s", propertyRequest);
        }

        if (fileLess) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new FileResponse(pdf.get().getName()));
        } else {
            try {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + propertyRequest.getNumber() + ".pdf")
                        .contentLength(pdf.get().length())
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new InputStreamResource(new FileInputStream(pdf.get())));
            } catch (FileNotFoundException e) {
                throw new PropertyStorageException("Couldn't generate a pdf");
            }
        }
    }

    @PostMapping("/pdf/all")
    public ResponseEntity<?> getPdf(@RequestBody List<PaymentCaseRecordRequestDto> records) throws ArchivalException {
        if (records.isEmpty()) return ResponseEntity.noContent().build();

        final List<Path> files = records.stream()
                .map(record -> propertyCache.getFilePath(
                        new PropertyRequest(record.getOffice(), record.getType(), record.getNumber(), record.getYears()), FileType.PDF)
                )
                .filter(path -> path.toFile().exists())
                .collect(Collectors.toList());

        final var archive = new Compressor(true)
                .compress(ArchiveType.ZIP, files, propertyCache.getSystemPath());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new FileResponse(archive.getName()));
    }

    @GetMapping("/pdf/all/file")
    public ResponseEntity<?> getAllPdfFiles(@RequestParam("name") String name) throws PropertyStorageException {
        var fileStream = propertyStorageService.getResource(name);

        if (fileStream.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + name)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream.get()));
    }
}
