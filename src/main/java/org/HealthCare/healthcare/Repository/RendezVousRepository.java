package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous , Long> {
}
