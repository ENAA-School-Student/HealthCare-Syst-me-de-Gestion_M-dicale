package org.HealthCare.healthcare.DTO.patient.rendezVous;

import lombok.Getter;
import lombok.Setter;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseRendezVousDTO {
    private Long id;
    private LocalDate dateRendezVous;
    private StatutRendezVous statut;
    private Long patientId;
    private Long medecinId;
}
