package ru.ineureka.patents.persistence.proxy;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "PROXY_SERVER_REQUEST_MONTH_CHART")
@Immutable
public final class ProxyServerChartPoint {
    @Id
    private String period;

    private int requests;

    public ProxyServerChartPoint(String period, int requests) {
        this.period = period;
        this.requests = requests;
    }

    protected ProxyServerChartPoint() {
    }

    public String getPeriod() {
        return period;
    }

    public int getRequests() {
        return requests;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }
}
