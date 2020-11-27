package ru.ineureka.patents.persistence.cases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyCaseFileDraftRepository extends JpaRepository<PropertyCaseFileDraft, Long> {

    @Query("SELECT " +
            "new ru.ineureka.patents.persistence.cases.PropertyCaseFileDraft(d.id, d.name, d.checksum, d.version, d.createdAt) " +
            "FROM PROPERTY_CASE_FILE_DRAFT d ORDER BY d.createdAt DESC")
    List<PropertyCaseFileDraft> getAllWithoutText();

    @Query("SELECT " +
            "new ru.ineureka.patents.persistence.cases.PropertyCaseFileDraft(d.id, d.name, d.checksum, d.version, d.createdAt) " +
            "FROM PROPERTY_CASE_FILE_DRAFT d " +
            "WHERE d.checksum = :hash ORDER BY d.createdAt DESC")
    List<PropertyCaseFileDraft> getAllByHashWithoutText(@Param("hash") String checksum);

    Optional<PropertyCaseFileDraft> getAllByIdOrderByCreatedAtDesc(long id);

    Optional<PropertyCaseFileDraft> getTopByOrderByCreatedAtDesc();

    Optional<PropertyCaseFileDraft> getTopByChecksumOrderByCreatedAtDesc(String hash);
}
