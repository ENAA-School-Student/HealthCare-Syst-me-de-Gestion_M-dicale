package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.DTO.patient.medecin.CountRdvDeMedecinDTO;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous , Long> {
    List<RendezVous> findByPatient_Nom(String nom);

    List<RendezVous> findByMedecin_Nom(String nom);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.id = :id AND r.dateRendezVous = :date")
    List<RendezVous> recuperDesRendezVousDeMedecinByDate(@Param("id") Long id, @Param("date")LocalDate date);

    @Query("SELECT m.nom , COUNT(r.id) AS nombre_rendezVous " +
            "FROM Medecin m JOIN RendezVous r ON m.id = r.medecin.id GROUP BY m.id , m.nom")
    List<CountRdvDeMedecinDTO> calculerLesRendezVousDeMedecin();
}
