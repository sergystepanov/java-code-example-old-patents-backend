package ru.ineureka.patents.service;

import com.google.common.flogger.FluentLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.persistence.proxy.*;
import ru.ineureka.patents.service.proxy.ProxyObject;
import ru.ineureka.patents.service.proxy.ProxyService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProxyServerService {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final ProxyService proxyService;
    private final ProxyServerRepository repository;
    private final ProxyServerRequestRepository proxyServerRequestRepository;
    private final ProxyServerRequestMonthChartRepository proxyServerRequestMonthChartRepository;

    public ProxyServerService(ProxyService proxyService,
                              ProxyServerRepository repository,
                              ProxyServerRequestRepository proxyServerRequestRepository,
                              ProxyServerRequestMonthChartRepository proxyServerRequestMonthChartRepository) {
        this.proxyService = proxyService;
        this.repository = repository;
        this.proxyServerRequestRepository = proxyServerRequestRepository;
        this.proxyServerRequestMonthChartRepository = proxyServerRequestMonthChartRepository;

        reloadProxy();
    }

    private void reloadProxy() {
        proxyService.load(repository.getAllEnabledWithTodayStats());
    }

    public List<ProxyServer> getAll() {
        return (List<ProxyServer>) repository.findAll();
    }

    public Optional<ProxyServer> getById(long id) {
        return repository.findById(id);
    }

    public List<ProxyServerChartPoint> getChart() {
        return proxyServerRequestMonthChartRepository.findAll();
    }

    @Transactional
    public boolean setServerEnabled(long id, boolean enabled) {
        final boolean result = repository.setEnabled(id, enabled) == 1;

        reloadProxy();

        return result;
    }

    /**
     * Fetches any free server from the database.
     *
     * @return A free server if it exists.
     */
    @Transactional
    public Optional<ProxyServer> getFree() {
        final Optional<ProxyObject> proxyObject = proxyService.get();

        if (proxyObject.isPresent()) {
            final var proxyCandidate = proxyObject.get();
            final var proxy = new ProxyServer(proxyCandidate.getId(), proxyCandidate.getIp());
            updateServerStats(proxyCandidate);

            return Optional.of(proxy);
        }

        return Optional.empty();
    }

    @Transactional
    public void updateAccessTime(ProxyServer proxy, Instant time) {
        Objects.requireNonNull(proxy);

        logger.atFine().log("Set current access time for server: %s", proxy);

        proxyService.updateAccessTime(proxy.getId(), time);
        repository.updateAccessTime(proxy.getId(), time);
    }

    @Async
    @Transactional
    void updateServerStats(ProxyObject proxy) {
        final var id = proxy.getId();
        final Optional<ProxyServerRequest> request = proxyServerRequestRepository.getCurrentByServerId(id);

        logger.atInfo().log("Update server [%s] stats", id);

        if (request.isPresent()) {
            proxyServerRequestRepository.increment(id);
        } else {
            proxyServerRequestRepository.save(new ProxyServerRequest(null, id, null, 1, proxy.getAccessedAt()));
        }
        repository.updateAccessTime(proxy.getId(), proxy.getAccessedAt());
    }
}
