package org.HealthCare.healthcare.Repository;

import org.HealthCare.healthcare.Entity.DossierMedical;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface DossierMedicalRepository extends JpaRepository<DossierMedical , Long> {
    @EntityGraph(attributePaths = "patient")
    Page<DossierMedical> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "patient")
    Optional<DossierMedical> findByPatient_Id(Long patientId);

    @EntityGraph(attributePaths = "patient")
    Optional<DossierMedical> findById(Long id);
}
