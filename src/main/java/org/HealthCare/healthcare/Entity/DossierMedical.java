package org.HealthCare.healthcare.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "dossier_medical")
@Getter
@Setter
public class DossierMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diagnostic;
    private String observation;
    private LocalDate dateCreation;

    public DossierMedical(String diagnostic, String observation, LocalDate dateCreation) {
        this.diagnostic = diagnostic;
        this.observation = observation;
        this.dateCreation = dateCreation;
    }

    public DossierMedical() {
    }

    @OneToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;
}
