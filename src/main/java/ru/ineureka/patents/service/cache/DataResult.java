package ru.ineureka.patents.service.cache;

public final class DataResult {
    public final byte[] data;
    public final boolean isCached;

    public DataResult(byte[] data, boolean isCached) {
        this.data = data;
        this.isCached = isCached;
    }
}
