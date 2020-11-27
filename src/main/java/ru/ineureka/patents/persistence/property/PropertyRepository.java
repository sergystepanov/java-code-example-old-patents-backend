package ru.ineureka.patents.persistence.property;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface PropertyRepository extends Repository<Property, Long> {
    Optional<Property> findByPatentOfficeAndTypeAndApplicationNumber(Long office, Long type, String applicationNumber);
}
