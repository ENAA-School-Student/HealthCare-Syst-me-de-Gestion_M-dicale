package org.HealthCare.healthcare.Repository;

import jdk.dynalink.linker.LinkerServices;
import org.HealthCare.healthcare.Entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin , Long> {
    @Query("SELECT m FROM Medecin m LEFT JOIN m.rendezVous rv where rv.id is null ")
    List<Medecin> afficherMedecinSansRdv();

    @Query("SELECT m FROM Medecin m WHERE m.specialite = :sepacialte")
    List<Medecin> afficherMedecinParsepacialte(@Param("sepacialte")String sepacialte);

    @Query("SELECT m FROM Medecin m JOIN m.rendezVous r WHERE r.dateRendezVous = CURRENT_DATE")
    List<Medecin> afficherMedecinAvecRdvAujourdui();
}
