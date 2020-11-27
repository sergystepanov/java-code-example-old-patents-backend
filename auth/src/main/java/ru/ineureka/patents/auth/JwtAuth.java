package ru.ineureka.patents.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtAuth implements Auth<Long> {
    private final Key key;
    private final int tokenTtl;
    private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;

    public JwtAuth(String key, int tokenTtl) {
        this.key = base64ToKey(key);
        this.tokenTtl = tokenTtl;
    }

    @Override
    public boolean validate(String token) throws AuthException {
        try {
            return !Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().isEmpty();
        } catch (SecurityException ignored) {
            throw new AuthException("Invalid JWT signature");
        } catch (MalformedJwtException ignored) {
            throw new AuthException("Invalid JWT token");
        } catch (ExpiredJwtException ignored) {
            throw new AuthException("Expired JWT token");
        } catch (UnsupportedJwtException ignored) {
            throw new AuthException("Unsupported JWT token");
        } catch (IllegalArgumentException ignored) {
            throw new AuthException("JWT claims string is empty");
        } catch (Exception ignored) {
            throw new AuthException("Jwt verification problem");
        }
    }

    @Override
    public Long getSubject(String token) {
        final var id = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(id);
    }

    @Override
    public String getToken(Long userId) {
        final var now = Instant.now();

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(tokenTtl)))
                .signWith(key, algorithm)
                .compact();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public static String keyToBase64(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static Key base64ToKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm.getJcaName());
    }

    public static String showMeAKey() {
        return keyToBase64(Keys.secretKeyFor(algorithm));
    }
}
