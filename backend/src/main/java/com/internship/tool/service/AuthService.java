package com.internship.tool.service;

import com.internship.tool.dto.AuthRequest;
import com.internship.tool.dto.AuthResponse;
import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;
import com.internship.tool.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    public void register(AuthRequest request) {

        // 🔐 Get role from request
        String role = request.getRole();

        // ✅ Default role = USER
        if (role == null || role.isBlank()) {
            role = "USER";
        }

        // ✅ Basic validation
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Invalid role");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
    }

    // LOGIN
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 MATCH PASSWORD
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 🔑 Include role in token
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );

        return new AuthResponse(token);
    }
}