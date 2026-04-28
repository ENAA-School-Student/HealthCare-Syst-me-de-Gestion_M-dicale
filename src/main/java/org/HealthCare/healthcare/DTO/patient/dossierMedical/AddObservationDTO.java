package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddObservationDTO {
    @NotBlank(message = "Le observation est obligatoire")
    private String observation;
}
