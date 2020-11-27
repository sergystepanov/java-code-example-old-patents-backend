package ru.ineureka.patents.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ineureka.patents.persistence.office.PatentOfficeFee;
import ru.ineureka.patents.service.office.OfficeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offices")
public class OfficeController {

    private final OfficeService officeService;
    final ObjectMapper mapper = new ObjectMapper();

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/fees")
    public List<PatentOfficeFee> getAllFees() {
        return officeService.getFees();
    }

    @GetMapping("/fees/keyed")
    public String getAllFeesOptimized() throws JsonProcessingException {
        return mapper.writeValueAsString(officeService.getAllOptimizedBySince(officeService.getFees()));
    }
}
