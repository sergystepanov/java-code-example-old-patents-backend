package ru.ineureka.patents.http.client;

import java.nio.charset.Charset;

public class DefaultHttpClientResult implements HttpClientResult<byte[]> {
    private final byte[] data;

    public DefaultHttpClientResult(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] get() {
        return data;
    }

    @Override
    public String asString(Charset encoding) {
        return new String(data, encoding);
    }
}
