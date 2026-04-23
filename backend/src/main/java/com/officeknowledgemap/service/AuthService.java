package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.AuthResponse;
import com.officeknowledgemap.dto.LoginRequest;
import com.officeknowledgemap.dto.RegisterRequest;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.repository.UserRepository;
import com.officeknowledgemap.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setContactInfo(request.getContactInfo());
        user.setRole(request.getRole());
        user.setPhotoUrl(request.getPhotoUrl());
        
        user = userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, String.valueOf(user.getId()), user.getUsername(), user.getName(), user.getRole());
    }
    
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, String.valueOf(user.getId()), user.getUsername(), user.getName(), user.getRole());
    }
}
