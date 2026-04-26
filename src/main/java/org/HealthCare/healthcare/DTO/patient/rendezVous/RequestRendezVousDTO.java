package org.HealthCare.healthcare.DTO.patient.rendezVous;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Data
public class RequestRendezVousDTO {
    @NotBlank(message = "Le date de rendez vous est obligatoire")
    private LocalDate dateRendezVous;
    @NotNull(message = "Le type de rendez vous est obligatoire")
    private StatutRendezVous statut;
}
