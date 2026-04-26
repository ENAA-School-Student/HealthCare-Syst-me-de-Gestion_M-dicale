package org.HealthCare.healthcare.DTO.patient.rendezVous;

import lombok.Data;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Data
public class ResponseRendezVousDTO {
    private Long id;
    private LocalDate dateRendezVous;
    private StatutRendezVous statut;
}
