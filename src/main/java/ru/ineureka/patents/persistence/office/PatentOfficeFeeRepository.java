package ru.ineureka.patents.persistence.office;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatentOfficeFeeRepository extends JpaRepository<PatentOfficeFee, Long> {
    List<PatentOfficeFee> getAllBy();
}
