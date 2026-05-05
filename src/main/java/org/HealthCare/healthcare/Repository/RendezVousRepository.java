package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.RendezVous;
import org.HealthCare.healthcare.enums.StatutRendezVous;
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
    List<RendezVous> afficherMedecinRendezVous(@Param("id")Long id , @Param("date")LocalDate date);

    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :id")
    List<RendezVous> afficherRdvPatient(@Param("id")Long id);

    @Query("SELECT p FROM Patient p JOIN p.rendezVous r GROUP BY p.id , p.nom , p.prenom HAVING COUNT(r.id) > :x")
    List<RendezVous> afficherRdvPatientAyantX(@Param("x")Long x);

    @Query("SELECT r FROM RendezVous r WHERE r.dateRendezVous = CURRENT_DATE")
    List<RendezVous> afficherRdvAujourdui();

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.specialite = :specialite AND r.statut = :statut")
    List<RendezVous> afficherRdvParSpecEtStatut(@Param("specialite")String specialite ,@Param("statut") StatutRendezVous statut);

    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.dateRendezVous = CURRENT_DATE")
    List<RendezVous> afficherRdvParDate();

    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :id ORDER BY r.dateRendezVous DESC")
    List<RendezVous> afficherDernierRdv(@Param("id")Long id);

    @Query("SELECT r FROM RendezVous r WHERE r.statut = :statut")
    List<RendezVous> afficherRdvParStatut(@Param("statut")StatutRendezVous statut);

    @Query("SELECT r FROM RendezVous r ORDER BY r.dateRendezVous DESC")
    List<RendezVous> trierRdv();

    @Query("SELECT COUNT(r) FROM RendezVous r")
    Long countRdv();

    @Query("SELECT r FROM RendezVous r WHERE r.dateRendezVous BETWEEN :date1 AND :date2")
    List<RendezVous> afficherRdvEntreDeuxDate(@Param("date1")LocalDate date1 , @Param("date2")LocalDate date2);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.specialite LIKE :specialite")
    List<RendezVous> afficherRdvParspecialite(@Param("specialite")String specialite);

    @Query("SELECT COUNT(p) FROM Patient p JOIN p.rendezVous r JOIN r.medecin m WHERE p.id = :id")
    Long countPatientDeMedecin(@Param("id")Long id);
}
