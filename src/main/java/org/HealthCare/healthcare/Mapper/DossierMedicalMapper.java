package org.HealthCare.healthcare.Mapper;

import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedecalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DossierMedicalMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "patient" , ignore = true)
    DossierMedical toEntity(RequestDossierMedecalDTO dto);
    ResponseDossierMedicalDTO toResponse(DossierMedical dossierMedical);
}
