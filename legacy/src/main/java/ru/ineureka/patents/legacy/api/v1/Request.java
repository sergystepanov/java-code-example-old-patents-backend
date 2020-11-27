package ru.ineureka.patents.legacy.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.common.flogger.FluentLogger;
import ru.ineureka.patents.http.client.HttpClient;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCaseDto;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckPayRequestDto;
import ru.ineureka.patents.legacy.api.v1.dto.PaymentCheckRecordDto;
import ru.ineureka.patents.legacy.api.v1.response.PayResponse;

import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Request {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final String remoteServiceURL;
    private final HttpClient<byte[]> httpClient;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.registerModule(new AfterburnerModule());
    }

    public Request(String remoteServiceURL, HttpClient<byte[]> httpClient) {
        this.remoteServiceURL = remoteServiceURL;
        this.httpClient = httpClient;
    }

    public List<PaymentCaseDto> getUnpaidDocuments() {
        try {
            return objectMapper.readValue(httpClient.get(remoteServiceURL + "/e").asString(UTF_8),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, PaymentCaseDto.class));
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("Legacy API [get-unpaid-documents] request error.");
        }

        return Collections.emptyList();
    }

    public List<PaymentCheckRecordDto> getUnpaidCase(String id) {
        try {
            var data = httpClient.get(remoteServiceURL + "/e" + (id.isEmpty() ? "" : "/" + id));
            return objectMapper.readValue(data.asString(UTF_8), objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, PaymentCheckRecordDto.class));
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("Legacy API [get-unpaid-case:%s] request error.", id);
        }

        return Collections.emptyList();
    }

    /**
     * Set {@code paid} flag for given list of payment cases.
     * <pre>POST /api/v1/pay?ids=1,2,3,4</pre>
     *
     * @param request A {@code PaymentCheckPayRequestDto} payment request.
     * @return The {@code PayResponse} object containing the number of cases paid.
     */
    public PayResponse payCase(PaymentCheckPayRequestDto request) {
        final String query = "ids=" + String.join(",", request.getIds());

        try {
            return objectMapper.readValue(httpClient.post(remoteServiceURL + "/pay", query).asString(UTF_8),
                    objectMapper.getTypeFactory().constructType(PayResponse.class));
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("Legacy API [pay-case] request error.");
        }

        return null;
    }
}
