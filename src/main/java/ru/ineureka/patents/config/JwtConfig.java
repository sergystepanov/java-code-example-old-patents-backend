package ru.ineureka.patents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${app.security.token.header:Authorization}")
    private String header;

    @Value("${app.security.token.prefix:Bearer }")
    private String prefix;

    @Value("${app.security.token.key}")
    private String secret;

    @Value("${app.security.token.ttl:#{24*60*60}}")
    private int ttl;

    @Value("${app.security.token.gen-on-start:false}")
    private boolean genOnStart;

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getTtl() {
        return ttl;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isGenOnStart() {
        return genOnStart;
    }
}
