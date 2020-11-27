package ru.ineureka.patents.persistence.cases;

import org.hibernate.annotations.CreationTimestamp;
import ru.ineureka.patents.persistence.client.Client;
import ru.ineureka.patents.persistence.user.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity(name = "PROPERTY_CASE")
public class PropertyCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;

    private Boolean closed;

    private Boolean archived;

    @OneToMany(mappedBy = "propertyCase", fetch = FetchType.LAZY)
    private List<PropertyCaseFile> files;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    protected PropertyCase() {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public List<PropertyCaseFile> getFiles() {
        return files;
    }

    public void setFiles(List<PropertyCaseFile> files) {
        this.files = files;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
