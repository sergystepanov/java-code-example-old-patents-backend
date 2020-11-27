package ru.ineureka.patents.persistence.office;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatentOfficeRepository extends JpaRepository<PatentOffice, Long> {
    List<PatentOffice> getAllBy();
}
