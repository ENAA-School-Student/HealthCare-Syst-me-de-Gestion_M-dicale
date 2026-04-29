package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.nom LIKE %:nom%")
    Patient recuperPatientbyNom(String nom);
}
