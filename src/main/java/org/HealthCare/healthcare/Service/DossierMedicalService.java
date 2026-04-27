package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedecalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.HealthCare.healthcare.Mapper.DossierMedicalMapper;
import org.HealthCare.healthcare.Repository.DossierMedicalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DossierMedicalService {
    private DossierMedicalRepository dossierMedicalRepository;
    private DossierMedicalMapper dossierMedicalMapper;

    public DossierMedicalService(DossierMedicalRepository dossierMedicalRepository , DossierMedicalMapper dossierMedicalMapper){
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.dossierMedicalMapper = dossierMedicalMapper;
    }

    public ResponseDossierMedicalDTO createDossierMedical(RequestDossierMedecalDTO dto){
        DossierMedical dossierMedicalMapperEntity = dossierMedicalMapper.toEntity(dto);
        dossierMedicalMapperEntity.setDateCreation(LocalDate.now());
        DossierMedical dossierMedical = dossierMedicalRepository.save(dossierMedicalMapperEntity);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    public ResponseDossierMedicalDTO addDiagnostic(Long id , RequestDossierMedecalDTO dto){
        DossierMedical dejaExists = getDossierOrThrow(id);
        dejaExists.setDiagnostic(dto.getDiagnostic());
        DossierMedical dossierMedical = dossierMedicalRepository.save(dejaExists);
        return dossierMedicalMapper.toResponse(dossierMedical);
    }

    public ResponseDossierMedicalDTO addObservation(Long id , RequestDossierMedecalDTO dto){
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
