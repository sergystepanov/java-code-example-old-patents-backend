package ru.ineureka.patents.service.cases;

import org.springframework.stereotype.Service;
import ru.ineureka.patents.persistence.cases.PropertyCaseFileDraft;
import ru.ineureka.patents.persistence.cases.PropertyCaseFileDraftRepository;

import java.util.List;

@Service
public class DraftCaseService {

    private final PropertyCaseFileDraftRepository propertyCaseFileDraftRepository;

    public DraftCaseService(PropertyCaseFileDraftRepository propertyCaseFileDraftRepository) {
        this.propertyCaseFileDraftRepository = propertyCaseFileDraftRepository;
    }

    public List<PropertyCaseFileDraft> getAllWithoutText() {
        return propertyCaseFileDraftRepository.getAllWithoutText();
    }

    public List<PropertyCaseFileDraft> getAllByHashWithoutText(String hash) {
        return propertyCaseFileDraftRepository.getAllByHashWithoutText(hash);
    }

    public PropertyCaseFileDraft getById(long id) {
        return propertyCaseFileDraftRepository.getAllByIdOrderByCreatedAtDesc(id).orElse(null);
    }

    public PropertyCaseFileDraft getLast() {
        return propertyCaseFileDraftRepository.getTopByOrderByCreatedAtDesc().orElse(null);
    }

    public PropertyCaseFileDraft getLastByHash(String hash) {
        return propertyCaseFileDraftRepository.getTopByChecksumOrderByCreatedAtDesc(hash).orElse(null);
    }

    public PropertyCaseFileDraft save(PropertyCaseFileDraft draft) {
        return propertyCaseFileDraftRepository.save(draft);
    }
}
