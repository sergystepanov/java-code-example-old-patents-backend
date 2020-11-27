package ru.ineureka.patents.dto.client;

import java.util.List;

public final class ClientCaseDto {
    private final List<CaseMessageDto> messages;
    private final List<ClientCaseRecordDto> records;

    public ClientCaseDto(List<ClientCaseRecordDto> records, List<CaseMessageDto> messages) {
        this.messages = messages;
        this.records = records;
    }

    public List<CaseMessageDto> getMessages() {
        return messages;
    }

    public List<ClientCaseRecordDto> getRecords() {
        return records;
    }
}
