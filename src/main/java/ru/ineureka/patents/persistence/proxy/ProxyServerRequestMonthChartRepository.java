package ru.ineureka.patents.persistence.proxy;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProxyServerRequestMonthChartRepository extends Repository<ProxyServerChartPoint, String> {
    List<ProxyServerChartPoint> findAll();
}
