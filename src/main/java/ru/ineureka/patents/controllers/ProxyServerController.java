package ru.ineureka.patents.controllers;

import com.google.common.flogger.FluentLogger;
import org.springframework.web.bind.annotation.*;
import ru.ineureka.patents.http.client.HttpClient;
import ru.ineureka.patents.http.client.JdkHttpClient;
import ru.ineureka.patents.http.client.exception.HttpClientException;
import ru.ineureka.patents.persistence.proxy.ProxyServer;
import ru.ineureka.patents.persistence.proxy.ProxyServerChartPoint;
import ru.ineureka.patents.service.ProxyServerService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;

@RestController
@RequestMapping("/api/v1/servers")
public class ProxyServerController {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final ProxyServerService proxyServerService;
    private final HttpClient<byte[]> httpClient;

    public ProxyServerController(ProxyServerService proxyServerService) {
        this.proxyServerService = proxyServerService;
        this.httpClient = new JdkHttpClient();
    }

    @GetMapping
    public List<ProxyServer> getAll() {
        return proxyServerService.getAll();
    }

    @GetMapping("/chart")
    public List<ProxyServerChartPoint> getPlotData() {
        return proxyServerService.getChart();
    }

    @PostMapping("/switch/{id}")
    public boolean switchServer(@PathVariable("id") long id, @RequestParam(name = "to", defaultValue = "0") byte value) {
        return proxyServerService.setServerEnabled(id, value == 1);
    }

    @GetMapping("/poke/{id}")
    public boolean pokeServer(@PathVariable("id") long id) {
        return proxyServerService.getById(id).map(server -> {
            final String address = "http://" + server.getIp().replace("/url", "") + "/s/hi";

            boolean pokeSuccess;
            try {
                pokeSuccess = "hi".equals(httpClient.get(address).asString(StandardCharsets.UTF_8));
            } catch (HttpClientException | IOException e) {
                pokeSuccess = false;
            }

            logger.at(pokeSuccess ? Level.INFO : Level.WARNING).log(
                    "Poke result of [%s] was %s", server.getIp(), (pokeSuccess ? "" : "un") + "successful"
            );

            return pokeSuccess;
        }).get();
    }
}
