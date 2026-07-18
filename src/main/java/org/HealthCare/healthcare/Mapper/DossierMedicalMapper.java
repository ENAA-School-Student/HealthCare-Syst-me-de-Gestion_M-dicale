package org.HealthCare.healthcare.Mapper;

import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedicalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DossierMedicalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    DossierMedical toEntity(RequestDossierMedicalDTO dto);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.nom", target = "patientNom")
    @Mapping(source = "patient.prenom", target = "patientPrenom")
    ResponseDossierMedicalDTO toResponse(DossierMedical dossierMedical);

}
