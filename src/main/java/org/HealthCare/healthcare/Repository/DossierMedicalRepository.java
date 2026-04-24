package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical , Long> {
}
