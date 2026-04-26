package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestDossierMedecalDTO {
    @NotBlank(message = "Le diagnostic est obligatoire")
    private String diagnostic;
    @NotBlank(message = "Le observation est obligatoire")
    private String observation;
    @NotNull(message = "Le date cration est obligatoire")
    private LocalDate dateCreation;
}
