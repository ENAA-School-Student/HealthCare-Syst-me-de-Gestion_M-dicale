package org.HealthCare.healthcare.DTO.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestPatientDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "Le prenom est obligatoire")
    private String prenom;
    @NotNull(message = "Le telephone est obligatoire")
    private Long telephone;
    @NotNull(message = "Le date de naissance est obligatoire")
    private LocalDate dateNaissance;
}
