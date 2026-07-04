package com.futurewiki.service;

import com.futurewiki.dto.request.LoginRequest;
import com.futurewiki.dto.request.RegisterRequest;
import com.futurewiki.dto.response.LoginResponse;
import com.futurewiki.dto.response.MessageResponse;
import com.futurewiki.entity.User;
import com.futurewiki.entity.enums.Role;
import com.futurewiki.security.jwt.JwtService;
import com.futurewiki.exception.AuthenticationException;
import com.futurewiki.exception.DuplicateResourceException;
import com.futurewiki.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository
            , PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void validateRegistration(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists.");
        }

    }

    public MessageResponse register(RegisterRequest request) {

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

        return new MessageResponse("User registered successfully.");
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new AuthenticationException("Invalid email or password."));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new AuthenticationException("Invalid email or password.");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }

}