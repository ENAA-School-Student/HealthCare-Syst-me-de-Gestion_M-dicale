package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.RendezVous;
import org.HealthCare.healthcare.enums.StatutRendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous , Long> {
    Page<RendezVous> findByPatient_Nom(String nom, Pageable pageable);

    Page<RendezVous> findByMedecin_Nom(String nom, Pageable pageable);

    Page<RendezVous> findByMedecin_IdAndDateRendezVous(Long medecinId, java.time.LocalDate date, Pageable pageable);

    Page<RendezVous> findByStatut(StatutRendezVous statut, Pageable pageable);

    Page<RendezVous> findAll(Pageable pageable);
}
