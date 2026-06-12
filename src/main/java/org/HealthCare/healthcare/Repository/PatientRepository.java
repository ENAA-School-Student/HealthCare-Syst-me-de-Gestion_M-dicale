package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.nom LIKE %:nom%")
    Page<Patient> recuperPatientbyNom(String nom, Pageable pageable);

    @EntityGraph(attributePaths = "rendezVous")
    @Query("SELECT p FROM Patient p WHERE p.id = :id")
    Optional<Patient> findByIdWithRendezVous(@Param("id") Long id);

    Page<Patient> findAll(Pageable pageable);
}
