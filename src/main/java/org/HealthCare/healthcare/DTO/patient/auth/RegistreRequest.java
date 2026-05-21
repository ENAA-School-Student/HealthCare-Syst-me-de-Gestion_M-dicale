package org.HealthCare.healthcare.DTO.patient.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.HealthCare.healthcare.enums.RoleUser;

@Data
public class RegistreRequest {
    @NotBlank(message = "user name obligatoire ")
    private String username;


    @NotBlank(message = "email obligatoire")
    @Email(message = "email invalid ")
    private String email;

    @NotNull(message = "role obligatoire")
    private RoleUser role;

    @NotBlank(message = "password obligatoire")
    private String password;
}
