package org.HealthCare.healthcare.DTO.patient.medecin;

import lombok.Data;

@Data
public class ResponseMedecinDTO {
    private Long id;
    private String nom;
    private String specialite;
    private String email;
    private Long telephone;
}
