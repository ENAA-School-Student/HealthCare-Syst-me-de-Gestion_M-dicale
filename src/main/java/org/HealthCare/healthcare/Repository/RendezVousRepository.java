package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous , Long> {
    List<RendezVous> findByPatient_Nom(String nom);

    List<RendezVous> findByMedecin_Nom(String nom);

}
