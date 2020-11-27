package ru.ineureka.patents.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ineureka.patents.documents.generator.office.GeneratorException;
import ru.ineureka.patents.documents.generator.office.OfficeDocumentGenerator;
import ru.ineureka.patents.payload.RenewalRequest;
import ru.ineureka.patents.service.office.rupto.maintenance.DocumentParams;
import ru.ineureka.patents.service.office.rupto.maintenance.MaintenanceFeeDocumentFactory;
import ru.ineureka.patents.service.property.type.PropertyType;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static ru.ineureka.patents.payload.RenewalRequest.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentsController {
    private final OfficeDocumentGenerator generator;

    public DocumentsController() {
        this.generator = new OfficeDocumentGenerator();
    }

    @PostMapping("renewal")
    public ResponseEntity<?> getRenewal(@Valid @RequestBody RenewalRequest request) throws GeneratorException {
        final var map = request.getMapping();
        final var params = new DocumentParams.Builder()
                .withPropertyType(PropertyType.DESIGN)
                .withPropertyStartDate(map.get(START_DATE))
                .withPropertyApplicationDate(map.get(APPLICATION_DATE))
                .withDocumentDate(map.get(DOCUMENT_DATE))
                .build();

        final var document = MaintenanceFeeDocumentFactory.get(params);
        final var out = new ByteArrayOutputStream();

        try (InputStream template = document.getTemplate()) {
            generator.generate(template, out, map);
        } catch (IOException e) {
            throw new GeneratorException("File template was not found");
        }
        final var data = out.toByteArray();

        return ResponseEntity.ok()
                .header("Content-Type", document.getTemplateMimeType())
                .contentLength(data.length)
                .body(data);
    }
}
