package org.HealthCare.healthcare.Mapper;

import org.HealthCare.healthcare.DTO.patient.medecin.PutMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.RequestMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.ResponseMedecinDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedecinMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "rendezVous" , ignore = true)
    Medecin toEntity(RequestMedecinDTO dto);
    ResponseMedecinDTO toResponseDTO(Medecin medecin);

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "rendezVous" , ignore = true)
    void updateMedecin(PutMedecinDTO dto , @MappingTarget Medecin medecin);
}
