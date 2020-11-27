package ru.ineureka.patents.persistence.proxy;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * The {@code ProxyServer} object represents a proxy server data.
 * All dates nad times should be with UTC time zone (offset).
 */
@Entity(name = "PROXY_SERVER")
public class ProxyServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String alias;
    private String platform;
    private String ip;
    private long traffic;
    private String status;
    private long requests;
    private boolean enabled;

    @Column(name = "accessed_at", nullable = false)
    private Instant accessedAt;

    protected ProxyServer() {
    }

    public ProxyServer(long id, String alias, String platform, String ip, long traffic, String status, long requests,
                       boolean enabled, Instant accessedAt) {
        this.id = id;
        this.alias = alias;
        this.platform = platform;
        this.ip = ip;
        this.traffic = traffic;
        this.status = status;
        this.requests = requests;
        this.enabled = enabled;
        this.accessedAt = accessedAt;
    }

    public ProxyServer(long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTraffic() {
        return traffic;
    }

    public void setTraffic(long traffic) {
        this.traffic = traffic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRequests() {
        return requests;
    }

    public void setRequests(long requests) {
        this.requests = requests;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Instant getAccessedAt() {
        return accessedAt;
    }

    public void setAccessedAt(Instant accessedAt) {
        this.accessedAt = accessedAt;
    }

    /**
     * Returns last access time as epoch ms.
     */
    public long getAccessedAtAsEpochMs() {
        try {
            return getAccessedAt().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        } catch (ArithmeticException e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyServer server = (ProxyServer) o;
        return id == server.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ServerDto{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", platform='" + platform + '\'' +
                ", ip='" + ip + '\'' +
                ", traffic=" + traffic +
                ", status='" + status + '\'' +
                ", requests=" + requests +
                ", enabled=" + enabled +
                ", accessedAt=" + accessedAt +
                '}';
    }
}
