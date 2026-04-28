package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseDossierMedicalDTO {
    private Long id;
    private String diagnostic;
    private String observation;
    private LocalDate dateCreation;
    private Long patientId;
}
