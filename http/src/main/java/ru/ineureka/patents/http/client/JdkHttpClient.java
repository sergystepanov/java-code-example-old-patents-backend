package ru.ineureka.patents.http.client;

import com.google.common.flogger.FluentLogger;
import ru.ineureka.patents.http.client.exception.HttpClientException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * A simple JDK-based HTTP client implementation.
 */
public final class JdkHttpClient implements HttpClient<byte[]> {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public HttpClientResult<byte[]> get(String url) throws HttpClientException {
        try (var stream = new URL(url).openStream()) {
            return new DefaultHttpClientResult(stream.readAllBytes());
        } catch (IOException e) {
            logger.atSevere().log(e.getMessage());
            throw new HttpClientException("GET fail");
        }
    }

    @Override
    public HttpClientResult<byte[]> post(String url, String query) throws HttpClientException {
        try {
            final var con = new URL(url).openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "Patents/3.0.0");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (var out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(query);
                out.flush();
            }

            try (var stream = con.getInputStream()) {
                return new DefaultHttpClientResult(stream.readAllBytes());
            }
        } catch (IOException e) {
            logger.atSevere().log(e.getMessage());
            throw new HttpClientException("POST fail");
        }
    }
}
