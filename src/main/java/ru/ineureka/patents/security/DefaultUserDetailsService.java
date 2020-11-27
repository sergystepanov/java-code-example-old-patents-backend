package ru.ineureka.patents.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ineureka.patents.persistence.user.UserRepository;

import javax.transaction.Transactional;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public DefaultUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return SecureUserDetails.from(userRepository.findByUsernameOrEmail(username, username).orElseThrow(() ->
                new UsernameNotFoundException("User with credentials: " + username + " not found")
        ));
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        return SecureUserDetails.from(userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User with id: " + id + " not found")
        ));
    }
}
