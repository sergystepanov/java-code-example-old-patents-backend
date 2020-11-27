package ru.ineureka.patents.controllers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ineureka.patents.security.CurrentUser;
import ru.ineureka.patents.security.SecureUserDetails;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @GetMapping("/me")
    public UserDetails getMe(@CurrentUser SecureUserDetails currentUser) {
        return currentUser;
    }
}
