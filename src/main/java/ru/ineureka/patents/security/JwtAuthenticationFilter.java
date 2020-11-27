package ru.ineureka.patents.security;

import com.google.common.flogger.FluentLogger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ineureka.patents.auth.Auth;
import ru.ineureka.patents.auth.JwtAuth;
import ru.ineureka.patents.config.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final JwtConfig jwtConfig;
    private final Auth<Long> auth;
    private final DefaultUserDetailsService defaultUserDetailsService;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, DefaultUserDetailsService defaultUserDetailsService) {
        this.jwtConfig = jwtConfig;
        this.auth = new JwtAuth(jwtConfig.getSecret(), jwtConfig.getTtl());
        this.defaultUserDetailsService = defaultUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var header = request.getHeader(jwtConfig.getHeader());

        if (Objects.isNull(header) || !header.startsWith(jwtConfig.getPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        final var token = header.replace(jwtConfig.getPrefix(), "");

        // !to add user's username and roles inside JWT claims, db exclude
        try {
            if (auth.validate(token)) {
                final var details = defaultUserDetailsService.loadUserById(auth.getSubject(token));
                final var authentication = new UsernamePasswordAuthenticationToken(
                        details, null, details.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.atSevere().withCause(e).log("Auth error: %s", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
