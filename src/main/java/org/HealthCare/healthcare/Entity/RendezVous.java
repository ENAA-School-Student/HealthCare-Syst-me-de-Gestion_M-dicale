package org.HealthCare.healthcare.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.HealthCare.healthcare.enums.StatutRendezVous;

import java.time.LocalDate;

@Entity
@Table(name = "rendezVous")
@Getter
@Setter
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateRendezVous;
    private StatutRendezVous statut;

    public RendezVous(LocalDate dateRendezVous, StatutRendezVous statut) {
        this.dateRendezVous = dateRendezVous;
        this.statut = statut;
    }

    public RendezVous() {
    }
}
