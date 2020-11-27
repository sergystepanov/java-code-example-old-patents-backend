package ru.ineureka.patents.http.client;

import ru.ineureka.patents.http.client.exception.HttpClientException;

/**
 * The {@code HttpClient} interface which describes
 * a HTTP client class.
 *
 * @param <T> A result object class of the client requests.
 */
public interface HttpClient<T> {
    HttpClientResult<T> get(String url) throws HttpClientException;

    HttpClientResult<T> post(String url, String query) throws HttpClientException;
}
