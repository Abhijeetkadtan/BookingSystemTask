package com.example.booking.service;

import com.example.booking.entity.AppUser;
import com.example.booking.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username) {
        AppUser user = (AppUser)this.userRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException("User not found");
        });
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(new String[]{user.getRole().name()}).build();
    }
}
