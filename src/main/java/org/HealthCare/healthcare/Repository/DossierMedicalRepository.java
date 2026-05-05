package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical , Long> {
    @Query("SELECT d FROM DossierMedical d JOIN Patient p ON d.patient.id = p.id")
    List<DossierMedical> afficherDMPatient();
}
