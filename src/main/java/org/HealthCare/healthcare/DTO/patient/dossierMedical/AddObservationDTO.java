package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddObservationDTO {
    @NotBlank(message = "Le observation est obligatoire")
    private String observation;
}
