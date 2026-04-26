package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PutDossierMedicalDTO {
    @NotBlank(message = "Le diagnostic est obligatoire")
    private String diagnostic;
    @NotBlank(message = "Le observation est obligatoire")
    private String observation;
    @NotBlank(message = "Le date cration est obligatoire")
    private LocalDate dateCreation;
}
