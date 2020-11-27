package ru.ineureka.patents.service.proxy;

import ru.ineureka.patents.persistence.proxy.ProxyServer;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProxyService {

    /**
     * Loads a list of servers into the service.
     *
     * @param servers The list of server data.
     */
    void load(List<ProxyServer> servers);

    /**
     * Tries to get the next available server from the service.
     */
    Optional<ProxyObject> get();

    /**
     * Updates access time of a server.
     *
     * @param id   The id of the server.
     * @param time The new time to set.
     */
    void updateAccessTime(long id, Instant time);

    /**
     * Returns the size of the internal list of servers.
     */
    int size();
}
