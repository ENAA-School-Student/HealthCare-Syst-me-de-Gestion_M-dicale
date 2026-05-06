package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDiagnosticDTO {
    @NotBlank(message = "Le diagnostic est obligatoire")
    private String diagnostic;
}
