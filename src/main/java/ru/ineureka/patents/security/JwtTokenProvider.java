package ru.ineureka.patents.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.ineureka.patents.auth.Auth;
import ru.ineureka.patents.auth.JwtAuth;
import ru.ineureka.patents.config.JwtConfig;

@Component
public class JwtTokenProvider {
    private final Auth<?> auth;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.auth = new JwtAuth(jwtConfig.getSecret(), jwtConfig.getTtl());
    }

    public String generateToken(Authentication authentication) {
        return auth.getToken(((SecureUserDetails) authentication.getPrincipal()).getId());
    }

    public String generateRefreshToken() {
        return auth.generateRefreshToken();
    }
}
