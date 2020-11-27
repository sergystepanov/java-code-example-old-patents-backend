package ru.ineureka.patents.controllers;

import com.google.common.flogger.FluentLogger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ineureka.patents.dto.spreadsheet.SpreadsheetDto;
import ru.ineureka.patents.exception.*;
import ru.ineureka.patents.persistence.client.ClientImportTemplateRepository;
import ru.ineureka.patents.reader.excel.ExcelTable;
import ru.ineureka.patents.reader.excel.exception.ExcelReaderException;
import ru.ineureka.patents.service.Reader;
import ru.ineureka.patents.service.cases.ImportCaseService;
import ru.ineureka.patents.service.cases.client.ClientCaseDto;
import ru.ineureka.patents.service.storage.StorageService;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/v1/spreadsheet")
public class SpreadsheetController {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final Reader reader;
    private final ImportCaseService importCaseService;
    private final ClientImportTemplateRepository clientImportTemplateRepository;
    private final StorageService storageService;

    public SpreadsheetController(Reader reader, ImportCaseService importCaseService,
                                 ClientImportTemplateRepository clientImportTemplateRepository,
                                 StorageService storageService) {
        this.reader = reader;
        this.importCaseService = importCaseService;
        this.clientImportTemplateRepository = clientImportTemplateRepository;
        this.storageService = storageService;
    }

    @PostMapping("/read")
    public SpreadsheetDto read(@RequestParam("file") MultipartFile file, @RequestParam("tid") Long templateId) {
        SpreadsheetDto spreadsheet = null;
        final var fileName = file.getOriginalFilename();

        try (var input = new ByteArrayInputStream(file.getInputStream().readAllBytes())) {
            final ExcelTable table = reader.read(input);

            final var template = clientImportTemplateRepository.findById(templateId).orElseThrow(() -> {
                throw new SpreadsheetTemplateError("NO_TPL");
            });

            final ClientCaseDto readCase = importCaseService.process(table, template);

            final String checksum = storageService.getHash(input);
            logger.atInfo().log("File %s hashed with %s", fileName, checksum);

            spreadsheet = new SpreadsheetDto(fileName, checksum, readCase);
        } catch (ExcelReaderException e) {
            logger.atSevere().withCause(e);

            switch (e.getType()) {
                case AN_ERROR:
                    throw new SpreadsheetError(e.getMessage());
                case NOT_XLS:
                    throw new UnsupportedFileError(e.getMessage());
                case TOO_OLD:
                    throw new SpreadsheetOldError(e.getMessage());
            }
        } catch (SpreadsheetTemplateError e) {
            throw e;
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("An error: %s", e.getMessage());
            throw new FileError(fileName);
        }

        return spreadsheet;
    }
}
