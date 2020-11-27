package ru.ineureka.patents.service.cases.client;

import ru.ineureka.patents.dto.patent.PropertyCaseRecordDto;
import ru.ineureka.patents.dto.patent.PropertyDto;

public final class ClientCaseRecordDto {

    private final long id;
    // Property data
    private final PropertyDto property;
    private final boolean property_cached;

    // Pay data
    // i.e. annuity
    private final PropertyCaseRecordDto record;

    public ClientCaseRecordDto(long id, PropertyDto property, boolean property_cached, PropertyCaseRecordDto record) {
        this.id = id;
        this.property = property;
        this.property_cached = property_cached;
        this.record = record;
    }

    public long getId() {
        return id;
    }

    public PropertyDto getProperty() {
        return property;
    }

    public boolean isProperty_cached() {
        return property_cached;
    }

    public PropertyCaseRecordDto getRecord() {
        return record;
    }
}
