package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddDiagnosticDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddObservationDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedicalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Mapper.DossierMedicalMapper;
import org.HealthCare.healthcare.Repository.DossierMedicalRepository;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
public class DossierMedicalService {
    private DossierMedicalRepository dossierMedicalRepository;
    private DossierMedicalMapper dossierMedicalMapper;
    private PatientRepository patientRepository;

    public DossierMedicalService(DossierMedicalRepository dossierMedicalRepository , DossierMedicalMapper dossierMedicalMapper , PatientRepository patientRepository){
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.dossierMedicalMapper = dossierMedicalMapper;
        this.patientRepository = patientRepository;
    }

    @CacheEvict(value = "dossiers", allEntries = true)
    public ResponseDossierMedicalDTO createDossierMedical(RequestDossierMedicalDTO dto){
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        DossierMedical dossierMedicalMapperEntity = dossierMedicalMapper.toEntity(dto);
        dossierMedicalMapperEntity.setDateCreation(LocalDate.now());
        dossierMedicalMapperEntity.setPatient(patient);
        DossierMedical dossierMedical = dossierMedicalRepository.save(dossierMedicalMapperEntity);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    @CacheEvict(value = "dossiers", allEntries = true)
    public ResponseDossierMedicalDTO addDiagnostic(Long id , AddDiagnosticDTO dto){
        DossierMedical dejaExists = getDossierOrThrow(id);
        dejaExists.setDiagnostic(dto.getDiagnostic());
        DossierMedical dossierMedical = dossierMedicalRepository.save(dejaExists);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    @CacheEvict(value = "dossiers", allEntries = true)
    public ResponseDossierMedicalDTO addObservation(Long id , AddObservationDTO dto){
        DossierMedical dejaExists = getDossierOrThrow(id);
        dejaExists.setObservation(dto.getObservation());
        DossierMedical dossierMedical = dossierMedicalRepository.save(dejaExists);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    @Cacheable(value = "dossiers", key = "#id")
    public ResponseDossierMedicalDTO getDossierById(Long id){
        DossierMedical dossierMedical = getDossierOrThrow(id);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    public DossierMedical getDossierOrThrow(Long id){
        return dossierMedicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier not found"));
    }

    @Cacheable(value = "dossiers", key = "#pageable.pageNumber")
    public Page<ResponseDossierMedicalDTO> getAllDossierMedical(Pageable pageable){
        Page<DossierMedical> page = dossierMedicalRepository.findAll(pageable);
        return page.map(dossierMedicalMapper::toResponse);
    }

    @Cacheable(value = "dossiers", key = "'patient-' + #patientId")
    public ResponseDossierMedicalDTO getDossierByPatientId(Long patientId) {
        DossierMedical dossier = dossierMedicalRepository.findByPatient_Id(patientId)
                .orElseThrow(() -> new RuntimeException("Dossier medical introuvable"));
        return dossierMedicalMapper.toResponse(dossier);
    }
}
