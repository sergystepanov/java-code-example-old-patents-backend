package ru.ineureka.patents.service.office;

import org.springframework.stereotype.Service;
import ru.ineureka.patents.persistence.office.PatentOffice;
import ru.ineureka.patents.persistence.office.PatentOfficeFee;
import ru.ineureka.patents.persistence.office.PatentOfficeFeeRepository;
import ru.ineureka.patents.persistence.office.PatentOfficeRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfficeService {
    private final PatentOfficeRepository patentOfficeRepository;
    private final PatentOfficeFeeRepository patentOfficeFeeRepository;

    public OfficeService(PatentOfficeRepository patentOfficeRepository,
                         PatentOfficeFeeRepository patentOfficeFeeRepository) {
        this.patentOfficeRepository = patentOfficeRepository;
        this.patentOfficeFeeRepository = patentOfficeFeeRepository;
    }

    public List<PatentOffice> getAllOffices() {
        return patentOfficeRepository.getAllBy();
    }

    public List<PatentOfficeFee> getFees() {
        return patentOfficeFeeRepository.getAllBy();
    }

    public Map<String, List<PatentOfficeFee>> getAllOptimizedBySince(List<PatentOfficeFee> fees) {
        return Objects.nonNull(fees) ? bySince(fees).entrySet().stream()
                .sorted(Map.Entry.<String, List<PatentOfficeFee>>comparingByKey().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new)) :
                Collections.emptyMap();
    }

    /**
     * Returns fees list grouped by the date of their appliance.
     *
     * @param fees The initial list of fees.
     */
    private Map<String, List<PatentOfficeFee>> bySince(List<PatentOfficeFee> fees) {
        final Map<String, List<PatentOfficeFee>> result = new HashMap<>();

        for (var fee : fees) {
            var ymdSinceKey = DateTimeFormatter.ISO_LOCAL_DATE.format(fee.getSince().toLocalDate());
            result.putIfAbsent(ymdSinceKey, new ArrayList<>());
            result.get(ymdSinceKey).add(fee);
        }

        return result;
    }
}
