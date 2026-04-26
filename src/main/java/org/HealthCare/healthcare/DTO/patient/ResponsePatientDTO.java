package org.HealthCare.healthcare.DTO.patient;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponsePatientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private Long telephone;
    private LocalDate dateNaissance;
}
