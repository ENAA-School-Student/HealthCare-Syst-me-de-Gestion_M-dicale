package org.HealthCare.healthcare.Mapper;
import org.HealthCare.healthcare.DTO.patient.PutPatientDTO;
import org.HealthCare.healthcare.DTO.patient.RequestPatientDTO;
import org.HealthCare.healthcare.Entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "rendezVous" , ignore = true)
    @Mapping(target = "dossierMedical" , ignore = true)
    Patient toEntity(RequestPatientDTO dto);
    RequestPatientDTO toResponseDTO(Patient patient);
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "rendezVous" , ignore = true)
    @Mapping(target = "dossierMedical" , ignore = true)
    void updatePatient(PutPatientDTO dto , @MappingTarget Patient patient);


}
