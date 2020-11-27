package ru.ineureka.patents.persistence.proxy;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ProxyServerRepository extends CrudRepository<ProxyServer, Long> {
    @Query(
            value = "SELECT s.id, s.alias, s.platform, s.ip, s.traffic, s.status, IFNULL(r.count, 0) as requests, s.enabled, s.accessed_at FROM PROXY_SERVER s\n" +
                    "LEFT JOIN PROXY_SERVER_REQUEST r ON r.SERVER_ID = s.ID AND r.DATE_ = CURRENT_DATE " +
                    "WHERE s.enabled = true",
            nativeQuery = true)
    List<ProxyServer> getAllEnabledWithTodayStats();

    @Modifying
    @Query("UPDATE PROXY_SERVER p SET p.enabled = :enabled WHERE p.id = :id")
    int setEnabled(@Param("id") long id, @Param("enabled") boolean enabled);

    @Modifying
    @Query("UPDATE PROXY_SERVER p SET p.accessedAt = :timestamp WHERE p.id = :id")
    void updateAccessTime(long id, Instant timestamp);
}
