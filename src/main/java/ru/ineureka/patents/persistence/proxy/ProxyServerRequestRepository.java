package ru.ineureka.patents.persistence.proxy;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProxyServerRequestRepository extends CrudRepository<ProxyServerRequest, Long> {

    @Query("SELECT r FROM PROXY_SERVER_REQUEST r WHERE r.date_ = CURRENT_DATE AND r.server_id = :id")
    Optional<ProxyServerRequest> getCurrentByServerId(@Param("id") long id);

    @Modifying
    @Query("UPDATE PROXY_SERVER_REQUEST r SET r.count=r.count+1, r.access = CURRENT_TIMESTAMP " +
            "WHERE r.date_ = CURRENT_DATE AND r.server_id = :serverId")
    void increment(@Param("serverId") long serverId);
}
