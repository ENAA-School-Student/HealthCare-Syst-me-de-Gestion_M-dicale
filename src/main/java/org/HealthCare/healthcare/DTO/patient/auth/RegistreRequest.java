package org.HealthCare.healthcare.DTO.patient.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistreRequest {
    @NotBlank(message = "user name obligatoire ")
    private String username;


    @NotBlank(message = "email obligatoire")
    @Email(message = "email invalid ")
    private String email;

    @NotBlank(message = "password obligatoire")
    private String password;
}
