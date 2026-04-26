package org.HealthCare.healthcare.DTO.patient.rendezVous;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Data
public class PutRendezVousDTO {
    @NotNull(message = "Le date de rendez vous est obligatoire")
    private LocalDate dateRendezVous;
    @NotNull(message = "Le type de rendez vous est obligatoire")
    private StatutRendezVous statut;
}
