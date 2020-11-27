package ru.ineureka.patents.dto.client;

import com.fasterxml.jackson.annotation.JsonRawValue;

public final class ClientImportTemplateDto {
    private final long id;
    private final long client_id;
    @JsonRawValue
    private final String field_map;
    @JsonRawValue
    private final String patent_type_map;

    public ClientImportTemplateDto(long id, long client_id, String field_map, String patent_type_map) {
        this.id = id;
        this.client_id = client_id;
        this.field_map = field_map;
        this.patent_type_map = patent_type_map;
    }

    public long getId() {
        return this.id;
    }

    public long getClient_id() {
        return this.client_id;
    }

    public String getField_map() {
        return this.field_map;
    }

    public String getPatent_type_map() {
        return this.patent_type_map;
    }

    public String toString() {
        return "ClientImportTemplateDto(id=" + this.getId() + ", client_id=" + this.getClient_id() + ", field_map=" + this.getField_map() + ", patent_type_map=" + this.getPatent_type_map() + ")";
    }
}
