package ru.ineureka.patents.auth;

public class AuthException extends Exception {
    private static final long serialVersionUID = 129935711856420903L;

    public AuthException(String message) {
        super(message);
    }
}
