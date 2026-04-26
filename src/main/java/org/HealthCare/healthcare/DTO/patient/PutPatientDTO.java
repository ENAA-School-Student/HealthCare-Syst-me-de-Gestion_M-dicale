package org.HealthCare.healthcare.DTO.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PutPatientDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotNull(message = "Le telephone est obligatoire")
    private Long telephone;
    @NotNull(message = "Le date de naissance est obligatoire")
    private LocalDate dateNaissance;
}
