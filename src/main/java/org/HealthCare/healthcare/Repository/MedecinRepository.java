package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin , Long> {
    Page<Medecin> findAll(Pageable pageable);
}
