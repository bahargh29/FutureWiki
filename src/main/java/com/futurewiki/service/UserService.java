package com.futurewiki.service;

import com.futurewiki.dto.request.RegisterRequest;
import com.futurewiki.entity.User;
import com.futurewiki.entity.enums.Role;
import com.futurewiki.exception.DuplicateResourceException;
import com.futurewiki.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository
            , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateRegistration(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists.");
        }

    }

    public void register(RegisterRequest request) {

        validateRegistration(
                request.getUsername(),
                request.getEmail()
        );

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        user.setPassword(encodedPassword);

        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

}