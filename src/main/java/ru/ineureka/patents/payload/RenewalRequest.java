package ru.ineureka.patents.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class RenewalRequest {

    @NotNull
    @NotBlank
    private final String registry;

    @NotNull
    private final Map<String, String> mapping;

    @JsonCreator
    public RenewalRequest(@JsonProperty("registry") String registry,
                          @JsonProperty("mapping") Map<String, String> mapping) {
        this.registry = registry;
        this.mapping = mapping;
    }

    public String getRegistry() {
        return registry;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public static final String START_DATE = "start_date";
    public static final String APPLICATION_DATE = "application_date";
    public static final String DOCUMENT_DATE = "document_date";
}
