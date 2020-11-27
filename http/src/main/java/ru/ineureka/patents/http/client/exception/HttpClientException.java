package ru.ineureka.patents.http.client.exception;

public final class HttpClientException extends Exception {
    private static final long serialVersionUID = 3691609320772506900L;

    public HttpClientException() {
    }

    public HttpClientException(String message) {
        super(message);
    }
}
