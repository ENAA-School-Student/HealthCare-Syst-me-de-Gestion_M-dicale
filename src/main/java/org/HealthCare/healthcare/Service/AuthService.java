package org.HealthCare.healthcare.Service;

import lombok.extern.slf4j.Slf4j;
import org.HealthCare.healthcare.DTO.patient.auth.AuthResponse;
import org.HealthCare.healthcare.DTO.patient.auth.LoginRequest;
import org.HealthCare.healthcare.DTO.patient.auth.RegistreRequest;
import org.HealthCare.healthcare.Entity.User;
import org.HealthCare.healthcare.Repository.UserRepository;
import org.HealthCare.healthcare.Security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository repo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegistreRequest request) {
        log.info("Attempting to register user: {}", request.getEmail());
        User existing = repo.findByEmail(request.getEmail());

        if (existing != null) {
            log.warn("Registration failed: Email {} already exists", request.getEmail());
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        repo.save(user);
        log.info("User {} registered successfully", user.getEmail());

        String token = jwtUtil.genereteToken(user.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

            authenticationManager.authenticate(authToken);
            log.info("Authentication successful for: {}", request.getEmail());

            String token = jwtUtil.genereteToken(request.getEmail());
            return new AuthResponse(token);
        } catch (Exception e) {
            log.error("Login failed for {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }
}
