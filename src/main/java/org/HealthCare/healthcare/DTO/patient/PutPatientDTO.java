package org.HealthCare.healthcare.DTO.patient;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PutPatientDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotBlank(message = "Le telephone est obligatoire")
    private Long telephone;
    @NotBlank(message = "Le date de naissance est obligatoire")
    private LocalDate dateNaissance;
}
