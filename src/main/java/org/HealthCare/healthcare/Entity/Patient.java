package org.HealthCare.healthcare.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private Long telephone;
    private LocalDate dateNaissance;

    public Patient(String nom, String prenom, Long telephone, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
    }

    public Patient() {
    }

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private List<RendezVous> rendezVous;

    @OneToOne(mappedBy = "patient" , cascade = CascadeType.ALL)
    @JsonIgnore
    private DossierMedical dossierMedical;
}