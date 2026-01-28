package com.example.booking.controller;

import com.example.booking.config.JwtUtil;
import com.example.booking.dto.LoginRequest;
import com.example.booking.dto.RegisterRequest;
import com.example.booking.entity.AppUser;
import com.example.booking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/auth"})
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping({"/register"})
    public String register(@RequestBody RegisterRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        this.userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping({"/login"})
    public String login(@RequestBody LoginRequest request) {
        AppUser user = (AppUser)this.userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            return new RuntimeException("Invalid credentials");
        });
        if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        } else {
            return this.jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        }
    }
}
