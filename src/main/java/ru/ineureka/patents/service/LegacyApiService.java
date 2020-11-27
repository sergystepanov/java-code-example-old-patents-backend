package ru.ineureka.patents.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.http.client.JdkHttpClient;
import ru.ineureka.patents.legacy.api.v1.Request;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCaseDto;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckPayRequestDto;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckRecordDto;
import ru.ineureka.patents.legacy.api.v1.response.PayResponse;

import java.util.List;

@Service
public class LegacyApiService {
    private final Request legacyApiRequest;

    public LegacyApiService(@Value("${app.service.legacy.url}") String url) {
        this.legacyApiRequest = new Request(url, new JdkHttpClient());
    }

    public List<PaymentCaseDto> getUnpaidDocuments() {
        return legacyApiRequest.getUnpaidDocuments();
    }

    public List<PaymentCheckRecordDto> getUnpaidCase(String id) {
        // !to add original mutator
        return legacyApiRequest.getUnpaidCase(id);
    }

    public PayResponse payCase(PaymentCheckPayRequestDto request) {
        return legacyApiRequest.payCase(request);
    }
}
