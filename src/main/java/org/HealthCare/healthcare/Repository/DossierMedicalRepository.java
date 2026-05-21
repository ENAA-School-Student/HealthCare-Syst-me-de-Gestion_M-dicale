package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.DossierMedical;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical , Long> {
    Page<DossierMedical> findAll(Pageable pageable);
}
