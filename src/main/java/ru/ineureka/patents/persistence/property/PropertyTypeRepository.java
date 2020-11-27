package ru.ineureka.patents.persistence.property;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {
    List<PropertyType> getAllBy();
}
