package org.HealthCare.healthcare.DTO.patient;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResponsePatientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private Long telephone;
    private LocalDate dateNaissance;
    private Long userId;
}
