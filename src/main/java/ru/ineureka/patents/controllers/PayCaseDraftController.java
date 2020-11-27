package ru.ineureka.patents.controllers;

import org.springframework.web.bind.annotation.*;
import ru.ineureka.patents.persistence.cases.PropertyCaseFileDraft;
import ru.ineureka.patents.service.cases.DraftCaseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pay-case-draft")
public class PayCaseDraftController {

    private final DraftCaseService draftCaseService;

    public PayCaseDraftController(DraftCaseService draftCaseService) {
        this.draftCaseService = draftCaseService;
    }

    @GetMapping
    public List<PropertyCaseFileDraft> getAll() {
        return draftCaseService.getAllWithoutText();
    }

    @GetMapping("/byHash/{hash}")
    public List<PropertyCaseFileDraft> getAllByHash(@PathVariable("hash") String hash) {
        return draftCaseService.getAllByHashWithoutText(hash);
    }

    @GetMapping("/byId/{id}")
    public PropertyCaseFileDraft getById(@PathVariable("id") long id) {
        return draftCaseService.getById(id);
    }

    @GetMapping("/last")
    public PropertyCaseFileDraft getLast() {
        return draftCaseService.getLast();
    }

    @GetMapping("/last/byHash/{hash}")
    public PropertyCaseFileDraft getLastByHash(@PathVariable("hash") String hash) {
        return draftCaseService.getLastByHash(hash);
    }

    @PostMapping
    public PropertyCaseFileDraft create(@RequestBody PropertyCaseFileDraft draft) {
        return draftCaseService.save(draft);
    }
}
