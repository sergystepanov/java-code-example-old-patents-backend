package ru.ineureka.patents.service.cases;

import ru.ineureka.patents.service.cases.client.CaseMessageDto;

import java.util.ArrayList;
import java.util.List;

public final class ImportCaseMessages {
    private int i;
    private List<CaseMessageDto> messages;

    public ImportCaseMessages() {
        this.i = 1;
        this.messages = new ArrayList<>();
    }

    public void addAll(List<CaseMessageDto> messages) {
        this.messages.addAll(messages);
    }

    public void addWarning(long for_id, String text) {
        this.messages.add(new CaseMessageDto(this.i++, for_id, "", "warning", text));
    }

    public void addError(long for_id, String text) {
        this.messages.add(new CaseMessageDto(this.i++, for_id, "", "error", text));
    }

    public void addWarningForField(long for_id, String field, String text) {
        this.messages.add(new CaseMessageDto(this.i++, for_id, field, "warning", text));
    }

    public void addErrorForField(long for_id, String field, String text) {
        this.messages.add(new CaseMessageDto(this.i++, for_id, field, "error", text));
    }

    public List<CaseMessageDto> getMessages() {
        return messages;
    }
}
