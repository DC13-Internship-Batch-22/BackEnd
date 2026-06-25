package com.batch22bd.BackEnd.Controller;

import com.batch22bd.BackEnd.DTO.request.LoginRequest;
import com.batch22bd.BackEnd.DTO.request.RefreshRequest;
import com.batch22bd.BackEnd.DTO.response.AuthResponse;
import com.batch22bd.BackEnd.Entity.User;
import com.batch22bd.BackEnd.Repository.UserRepository;
import com.batch22bd.BackEnd.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // ===== LOGIN =====
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return new AuthResponse(accessToken, refreshToken);
    }

    // ===== REFRESH TOKEN =====
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);

        userRepository.findByUsername(username)
                .orElseThrow();

        String newAccessToken = jwtService.generateAccessToken(username);

        return new AuthResponse(newAccessToken, refreshToken);
    }
}