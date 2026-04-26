package org.HealthCare.healthcare.DTO.patient.medecin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PutMedecinDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le specialite est obligatoire")
    private String specialite;
    @NotBlank(message = "Le email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;
    @NotNull(message = "Le telephone est obligatoire")
    private Long telephone;
}
