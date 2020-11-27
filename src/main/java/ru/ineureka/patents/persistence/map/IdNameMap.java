package ru.ineureka.patents.persistence.map;

import java.util.Map;

public final class IdNameMap {
    public static String getById(long id, Map<Long, String> map) {
        return map.getOrDefault(id, "");
    }

    public static long getByName(String name, Map<Long, String> map) {
        return map.entrySet().stream()
                .filter(x -> x.getValue().equals(name))
                .findFirst()
                .map(Map.Entry::getKey).orElse(0L);
    }
}
