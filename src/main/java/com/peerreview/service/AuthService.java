package com.peerreview.service;

import com.peerreview.dto.AuthDTOs.AuthResponse;
import com.peerreview.dto.AuthDTOs.LoginRequest;
import com.peerreview.dto.AuthDTOs.RegisterRequest;
import com.peerreview.entity.User;
import com.peerreview.entity.User.Role;
import com.peerreview.repository.UserRepository;
import com.peerreview.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    // ── Register ──────────────────────────────────────────────────────────────
    public AuthResponse register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Account already exists!");
        }

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match!");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.valueOf(req.getRole()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    public AuthResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password!"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Email or Password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().name());
    }
}
