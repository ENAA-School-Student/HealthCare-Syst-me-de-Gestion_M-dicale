package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.HealthCare.healthcare.DTO.patient.auth.AuthResponse;
import org.HealthCare.healthcare.DTO.patient.auth.LoginRequest;
import org.HealthCare.healthcare.DTO.patient.auth.RegistreRequest;
import org.HealthCare.healthcare.Service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegistreRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
