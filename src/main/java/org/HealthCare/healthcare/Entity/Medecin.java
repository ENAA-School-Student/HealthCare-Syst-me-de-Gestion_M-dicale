package org.HealthCare.healthcare.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "medecin")
@Getter
@Setter
public class Medecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String specialite;
    private String email;
    private Long telephone;

    public Medecin(String nom, String specialite, String email, Long telephone) {
        this.nom = nom;
        this.specialite = specialite;
        this.email = email;
        this.telephone = telephone;
    }

    public Medecin() {
    }

    @OneToMany(mappedBy = "medecin")
    @JsonIgnore
    private List<RendezVous> rendezVous;
}
