package ru.ineureka.patents.persistence.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "PROXY_SERVER_REQUEST")
public final class ProxyServerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long server_id;

    @Column(name = "date_", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate date_;

    private int count;

    @UpdateTimestamp
    private Instant access;

    protected ProxyServerRequest() {
    }

    @JsonCreator
    public ProxyServerRequest(@JsonProperty("id") Long id,
                              @JsonProperty("server_id") Long server_id,
                              @JsonProperty("date_") LocalDate date_,
                              @JsonProperty("count") int count,
                              @JsonProperty("access") Instant access) {
        this.id = id;
        this.server_id = server_id;
        this.date_ = date_;
        this.count = count;
        this.access = access;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServer_id() {
        return server_id;
    }

    public void setServer_id(Long server_id) {
        this.server_id = server_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getDate_() {
        return date_;
    }

    public Instant getAccess() {
        return access;
    }

    public void setDate_(LocalDate date_) {
        this.date_ = date_;
    }

    public void setAccess(Instant access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "id=" + id +
                ", server_id=" + server_id +
                ", date_='" + date_ + '\'' +
                ", count=" + count +
                ", access='" + access + '\'' +
                '}';
    }
}
