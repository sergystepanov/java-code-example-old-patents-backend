package ru.ineureka.patents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    private final int lockSec;
    private final int waitSec;
    private final int dailyRequestLimit;
    private final String zone;

    public ProxyConfig(
            @Value("${app.proxy.lock-sec:120}") int lockSec,
            @Value("${app.proxy.wait-sec:7}") int waitSec,
            @Value("${app.proxy.daily-request-limit:950}") int dailyRequestLimit,
            @Value("${app.proxy.zone:Europe/Moscow}") String zone) {
        this.lockSec = lockSec;
        this.waitSec = waitSec;
        this.dailyRequestLimit = dailyRequestLimit;
        this.zone = zone;
    }

    public int getLockSec() {
        return lockSec;
    }

    public int getWaitSec() {
        return waitSec;
    }

    public int getDailyRequestLimit() {
        return dailyRequestLimit;
    }

    public String getZone() {
        return zone;
    }
}
