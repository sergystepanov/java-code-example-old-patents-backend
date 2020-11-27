package ru.ineureka.patents.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.ineureka.patents.payload.LoginRequest;
import ru.ineureka.patents.payload.LoginResponse;
import ru.ineureka.patents.payload.RegisterRequest;
import ru.ineureka.patents.persistence.role.Role;
import ru.ineureka.patents.persistence.role.RoleName;
import ru.ineureka.patents.persistence.role.RoleRepository;
import ru.ineureka.patents.persistence.user.User;
import ru.ineureka.patents.persistence.user.UserRepository;
import ru.ineureka.patents.security.JwtTokenProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var cookie = new Cookie("refreshToken", jwtTokenProvider.generateRefreshToken());
        cookie.setMaxAge(7 * 24 * 60 * 60);
        // cookie.setSecure(true);
        cookie.setHttpOnly(true);
        // cookie.setPath("/");
        response.addCookie(cookie);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email Address already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = new User(request.getName(), request.getUsername(), request.getEmail(), request.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User Role not set"));

        user.setRoles(Set.of(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body("User registered successfully");
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        // !to implement
        // same as /login
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/token/destroy")
    public ResponseEntity<?> destroyToken(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
