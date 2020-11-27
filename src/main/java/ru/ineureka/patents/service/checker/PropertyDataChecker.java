package ru.ineureka.patents.service.checker;

import ru.ineureka.patents.service.cases.client.CaseMessageDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PropertyDataChecker {

    public List<CaseMessageDto> checkAnnuity(Map<String, String> data) {
        // TODO realization
        return new ArrayList<>();
    }

    public List<CaseMessageDto> checkPaid(Map<String, String> data) {
        return new ArrayList<>();
    }

    public List<CaseMessageDto> checkDuplicates(List<Map<String, String>> data) {
        return new ArrayList<>();
    }
}
