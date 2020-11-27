package ru.ineureka.patents.controllers;

import org.springframework.web.bind.annotation.*;
import ru.ineureka.patents.documents.FileType;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCaseDto;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckPayRequestDto;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckRecordDto;
import ru.ineureka.patents.legacy.api.v1.response.PayResponse;
import ru.ineureka.patents.service.LegacyApiService;
import ru.ineureka.patents.service.cache.PropertyCache;
import ru.ineureka.patents.service.property.PropertyRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment_check")
public class PaymentCheckController {

    private final LegacyApiService legacyApiService;
    private final PropertyCache propertyCache;

    public PaymentCheckController(LegacyApiService legacyApiService, PropertyCache propertyCache) {
        this.legacyApiService = legacyApiService;
        this.propertyCache = propertyCache;
    }

    @GetMapping("/unpaid-documents/list")
    public List<PaymentCaseDto> getUnpaidDocuments() {
        return legacyApiService.getUnpaidDocuments();
    }

    @GetMapping("/unpaid-documents/case")
    public List<PaymentCheckRecordDto> getUnpaidCase(@RequestParam(name = "name", defaultValue = "") String id) {
        final List<PaymentCheckRecordDto> records = legacyApiService.getUnpaidCase(id);

        // update cache data
        for (PaymentCheckRecordDto record : records) {
            final String years = (record.getAnnuity() != null ? record.getAnnuity() : "") +
                    (record.getAnnuityEnd() != null ? "_" + record.getAnnuityEnd() : "");
            final PropertyRequest prop = new PropertyRequest(
                    record.getRegistry(), record.getType(), record.getRegistrationNumber(), years);
            record.setCached(propertyCache.isCached(prop));

            final var pdfPath = propertyCache.getFilePath(prop, FileType.PDF);
            if (pdfPath.toFile().exists()) {
                record.setPdf(true);
            }
        }

        return records;
    }

    @PostMapping("/pay")
    public PayResponse pay(@RequestBody PaymentCheckPayRequestDto request) {
        return legacyApiService.payCase(request);
    }
}
