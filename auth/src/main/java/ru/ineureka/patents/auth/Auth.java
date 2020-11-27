package ru.ineureka.patents.auth;

public interface Auth<T> {
    /**
     * Whether the authentication is successful or not.
     */
    boolean validate(String token) throws AuthException;

    /**
     * Returns token subject of type {@code T}.
     */
    T getSubject(String token);

    /**
     * Returns a token with provided credentials.
     *
     * @param userId A user id to encode.
     */
    String getToken(Long userId);

    /**
     * Returns random refresh token.
     */
    String generateRefreshToken();
}
