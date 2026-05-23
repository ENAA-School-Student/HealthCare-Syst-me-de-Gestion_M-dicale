package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.nom LIKE %:nom%")
    Page<Patient> recuperPatientbyNom(String nom, Pageable pageable);

    Page<Patient> findAll(Pageable pageable);
}
