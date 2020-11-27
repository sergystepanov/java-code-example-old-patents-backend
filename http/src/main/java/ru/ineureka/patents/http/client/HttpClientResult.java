package ru.ineureka.patents.http.client;

import java.io.IOException;
import java.nio.charset.Charset;

public interface HttpClientResult<T> {
    T get();

    String asString(Charset encoding) throws IOException;
}
