package ru.ineureka.patents.service;

import com.google.common.flogger.FluentLogger;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.http.client.HttpClient;
import ru.ineureka.patents.http.client.JdkHttpClient;
import ru.ineureka.patents.http.client.exception.HttpClientException;
import ru.ineureka.patents.office.Office;
import ru.ineureka.patents.office.org.eapo.Eapo;
import ru.ineureka.patents.office.org.eapo.EapoPublicationNumberSearch;
import ru.ineureka.patents.office.org.fips.Fips;
import ru.ineureka.patents.persistence.proxy.ProxyServer;
import ru.ineureka.patents.service.cache.DataResult;
import ru.ineureka.patents.service.cache.PropertyCache;
import ru.ineureka.patents.service.exception.ProxyError;
import ru.ineureka.patents.service.exception.RemoteRegistryRequestError;
import ru.ineureka.patents.service.property.PropertyRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Service
public class PatentOfficeProxy {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final ProxyServerService proxyServerService;
    private final PropertyCache cache;

    private static final int SLEEP_TIME_MS = 10;
    private static final int TOTAL_WAIT_TIME_MS = 30 * 1000;
    private final HttpClient<byte[]> httpClient;

    public PatentOfficeProxy(ProxyServerService proxyServerService, PropertyCache cache) throws ProxyError {
        this.proxyServerService = proxyServerService;
        this.cache = cache;
        this.httpClient = new JdkHttpClient();
    }

    public DataResult getInput(PropertyRequest propertyRequest) throws IOException {
        // Try the cache
        final Optional<File> cache = this.cache.get(propertyRequest);
        if (cache.isPresent()) {
            try (var stream = new FileInputStream(cache.get())) {
                return new DataResult(stream.readAllBytes(), true);
            }
        }

        String url;
        var skipProxy = false;

        switch (propertyRequest.getOffice()) {
            case Office.FIPS:
                url = Fips.getUrl(propertyRequest.getType(), propertyRequest.getNumber());
                break;
            case Office.EAPO:
                url = Eapo.getUrl(propertyRequest.getNumber());
                skipProxy = true;
                break;
            case Office.EAPO_SEARCH:
                url = EapoPublicationNumberSearch.getUrl(propertyRequest.getNumber());
                skipProxy = true;
                break;
            default:
                throw new ProxyError("Неизвестный реестр.");
        }
        logger.atFine().log("Url: %s", url);

        final var server = getServer().orElseThrow(() -> new ProxyError("Не удалось найти свободный сервер."));
        final var out = getConnection(server, url, skipProxy);

        if (!skipProxy) {
            proxyServerService.updateAccessTime(server, Instant.now());
        }

        return new DataResult(out, false);
    }

    /**
     * Finds an available server for a request.
     *
     * @return {@code Optional<ProxyServer>} server's data.
     * @throws ProxyError In case of servers unavailable.
     * @since 3.0.0
     */
    private Optional<ProxyServer> getServer() {
        final var startedAt = Instant.now().toEpochMilli();
        Optional<ProxyServer> server;
        var wasBusy = false;

        try {
            do {
                if (wasBusy) {
                    Thread.sleep(SLEEP_TIME_MS);
                } else {
                    wasBusy = true;
                }

                server = proxyServerService.getFree();
            } while (server.isEmpty() && Instant.now().toEpochMilli() - startedAt < TOTAL_WAIT_TIME_MS);

            logger.atInfo().log("Free server found: %s", server.isPresent() ? server.get().getIp() : "[x]");

            return server;
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("Couldn't get a server");
            throw new ProxyError(e, e.getMessage());
        }
    }

    public void save(PropertyRequest propertyRequest, byte[] bytes) {
        cache.put(propertyRequest, bytes);
    }

    private byte[] getConnection(ProxyServer server, String url, boolean direct) throws RemoteRegistryRequestError {
        try {
            return (direct ?
                    httpClient.get(url) :
                    httpClient.post("http://" + server.getIp(), "_url=" + URLEncoder.encode(url, StandardCharsets.UTF_8)))
                    .get();
        } catch (HttpClientException e) {
            throw new RemoteRegistryRequestError("");
        }
    }
}
