package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddDiagnosticDTO {
    @NotBlank(message = "Le diagnostic est obligatoire")
    private String diagnostic;
}
