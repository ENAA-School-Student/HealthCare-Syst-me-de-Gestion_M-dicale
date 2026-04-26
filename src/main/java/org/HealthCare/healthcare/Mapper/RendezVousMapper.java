package org.HealthCare.healthcare.Mapper;

import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RendezVousMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "patient" , ignore = true)
    @Mapping(target = "medecin" , ignore = true)
    RendezVous toEntity(RequestRendezVousDTO dto);
    ResponseRendezVousDTO toResponseDTO(RendezVous rendezVous);
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "patient" , ignore = true)
    @Mapping(target = "medecin" , ignore = true)
    void updateRendeVous(PutRendezVousDTO dto , @MappingTarget RendezVous rendezVous);
}
