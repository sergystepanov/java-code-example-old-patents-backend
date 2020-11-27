package ru.ineureka.patents.service.proxy;

import com.google.common.flogger.FluentLogger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.config.ProxyConfig;
import ru.ineureka.patents.persistence.proxy.ProxyServer;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A memory-based proxy server load balancer.
 * ThreadSafe
 *
 * @since 3.0.0
 */
@Service
public class MemoryProxyService implements ProxyService {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final ProxyConfig proxyConfig;

    // ordered set by access time, day requests, and object id
    private final NavigableSet<ProxyObject> servers = new ConcurrentSkipListSet<>(
            Comparator.comparing(ProxyObject::getAccessedAt)
                    .thenComparing(ProxyObject::getCount)
                    .thenComparing(ProxyObject::getId)
    );

    public MemoryProxyService(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public void load(List<ProxyServer> servers) {
        if (servers == null) return;

        this.servers.clear();

        for (var s : servers) {
            if (s.isEnabled()) {
                this.servers.add(new ProxyObject(s.getId(), s.getIp(), s.getAccessedAt(), s.getRequests()));
            }
        }

        logger.atInfo().log("(Re)Loaded %d servers", size());
    }

    @Override
    public Optional<ProxyObject> get() {
        ProxyObject result = null;

        // a simple round-robin server selecting algorithm
        for (var proxy : servers) {
            result = proxy.updateAndGetIf(
                    proxyConfig.getLockSec(),
                    p -> p.isAccessedLaterThan(proxyConfig.getWaitSec() * 1000),
                    p -> p.getCount() < proxyConfig.getDailyRequestLimit()
            );

            if (result != null) {
                logger.atFine().log("Got -> %s", result);
                break;
            }
        }

        return Optional.ofNullable(result);
    }

    // !to make it O(1)
    @Override
    public void updateAccessTime(long id, Instant time) {
        if (id < 1) return;

        for (var proxy : servers) {
            if (proxy.getId() == id) {
                proxy.setAccessedAt(time);
                break;
            }
        }
    }

    @Override
    public int size() {
        return servers.size();
    }

    @Scheduled(cron = "0 1 1 * * *", zone = "${app.service.proxy.zone}")
    private void resetProxyDailyLimitTracking() {
        logger.atInfo().log("Starting scheduled constraint reset");

        for (var proxy : servers) {
            proxy.reset();
        }
    }
}
