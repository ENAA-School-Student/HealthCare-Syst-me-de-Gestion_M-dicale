package org.HealthCare.healthcare.DTO.patient.rendezVous;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Data
public class PutRendezVousDTO {
    @NotNull(message = "Le patient_id est obligatoire")
    private Long patientId;
    @NotNull(message = "Le medecin_id est obligatoire")
    private Long medecinId;
    @NotNull(message = "Le date de rendez vous est obligatoire")
    private LocalDate dateRendezVous;
    @NotNull(message = "Le type de rendez vous est obligatoire")
    private StatutRendezVous statut;
}
