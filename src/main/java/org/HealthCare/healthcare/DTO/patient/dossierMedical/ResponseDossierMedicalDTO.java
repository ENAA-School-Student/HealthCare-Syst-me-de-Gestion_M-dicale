package org.HealthCare.healthcare.DTO.patient.dossierMedical;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseDossierMedicalDTO {

    private Long id;

    private String diagnostic;

    private String observation;

    private LocalDate dateCreation;

    private Long patientId;

    private String patientNom;

    private String patientPrenom;

}
