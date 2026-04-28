package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddDiagnosticDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddObservationDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedecalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Mapper.DossierMedicalMapper;
import org.HealthCare.healthcare.Repository.DossierMedicalRepository;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public ResponseDossierMedicalDTO createDossierMedical(RequestDossierMedecalDTO dto){
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        DossierMedical dossierMedicalMapperEntity = dossierMedicalMapper.toEntity(dto);
        dossierMedicalMapperEntity.setDateCreation(LocalDate.now());
        dossierMedicalMapperEntity.setPatient(patient);
        DossierMedical dossierMedical = dossierMedicalRepository.save(dossierMedicalMapperEntity);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    public ResponseDossierMedicalDTO addDiagnostic(Long id , AddDiagnosticDTO dto){
        DossierMedical dejaExists = getDossierOrThrow(id);
        dejaExists.setDiagnostic(dto.getDiagnostic());
        DossierMedical dossierMedical = dossierMedicalRepository.save(dejaExists);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    public ResponseDossierMedicalDTO addObservation(Long id , AddObservationDTO dto){
        DossierMedical dejaExists = getDossierOrThrow(id);
        dejaExists.setObservation(dto.getObservation());
        DossierMedical dossierMedical = dossierMedicalRepository.save(dejaExists);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    public ResponseDossierMedicalDTO getDossierById(Long id){
        DossierMedical dossierMedical = getDossierOrThrow(id);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    private DossierMedical getDossierOrThrow(Long id){
        return dossierMedicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier not found"));
    }
}
