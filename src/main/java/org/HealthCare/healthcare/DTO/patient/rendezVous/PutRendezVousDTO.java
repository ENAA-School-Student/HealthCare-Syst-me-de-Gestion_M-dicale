package org.HealthCare.healthcare.DTO.patient.rendezVous;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Getter
@Setter
public class PutRendezVousDTO {
    private Long patientId;
    private Long medecinId;
    @NotNull(message = "Le date de rendez vous est obligatoire")
    private LocalDate dateRendezVous;
    @NotNull(message = "Le statut est obligatoire")
    private StatutRendezVous statut;
}
