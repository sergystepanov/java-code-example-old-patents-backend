package ru.ineureka.patents.persistence.cases;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PROPERTY_CASE_FILE_DRAFT")
public class PropertyCaseFileDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String checksum;

    private String text;

    private String version;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    protected PropertyCaseFileDraft() {
    }

    @JsonCreator
    public PropertyCaseFileDraft(@JsonProperty("id") Long id,
                                 @JsonProperty("name") String name,
                                 @JsonProperty("checksum") String checksum,
                                 @JsonProperty("version") String version,
                                 @JsonProperty("created_at") Date createdAt) {
        this.id = id;
        this.name = name;
        this.checksum = checksum;
        this.version = version;
        this.text = "";
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
