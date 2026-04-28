package org.HealthCare.healthcare.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Entity
@Table(name = "rendez_vous")
@Getter
@Setter
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateRendezVous;
    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut;

    public RendezVous(LocalDate dateRendezVous, StatutRendezVous statut) {
        this.dateRendezVous = dateRendezVous;
        this.statut = statut;
    }

    public RendezVous() {
    }

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    @JsonIgnore
    private Medecin medecin;
}
