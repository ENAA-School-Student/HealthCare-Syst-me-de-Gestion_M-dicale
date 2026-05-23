package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.HealthCare.healthcare.Mapper.RendezVousMapper;
import org.HealthCare.healthcare.Repository.MedecinRepository;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.HealthCare.healthcare.Repository.RendezVousRepository;
import org.HealthCare.healthcare.enums.StatutRendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendezVousService {
    private RendezVousRepository rendezVousRepository;
    private RendezVousMapper rendezVousMapper;
    private PatientRepository patientRepository;
    private MedecinRepository medecinRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository , RendezVousMapper rendezVousMapper , PatientRepository patientRepository , MedecinRepository medecinRepository){
        this.rendezVousRepository = rendezVousRepository;
        this.rendezVousMapper = rendezVousMapper;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
    }

    public ResponseRendezVousDTO createRendezVous(RequestRendezVousDTO dto){
        Patient patient = patientRepository.findById(dto.getPatientId()).
                orElseThrow(()-> new RuntimeException("Patient not found"));
        Medecin medecin = medecinRepository.findById(dto.getMedecinId()).
                orElseThrow(()-> new RuntimeException("Medecin not found"));
        RendezVous rendezVousMapperEntity = rendezVousMapper.toEntity(dto);
        rendezVousMapperEntity.setPatient(patient);
        rendezVousMapperEntity.setMedecin(medecin);
        RendezVous rendezVous = rendezVousRepository.save(rendezVousMapperEntity);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    public ResponseRendezVousDTO updateRendezVous(Long id , PutRendezVousDTO dto){
        RendezVous dejaExists = rendezVousRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Rendez Vous not found"));
        rendezVousMapper.updateRendeVous(dto , dejaExists);
        RendezVous rendezVous = rendezVousRepository.save(dejaExists);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    public ResponseRendezVousDTO annulerRendezVous(Long id){
        RendezVous dejaExists = rendezVousRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Rendez Vous not found"));
        dejaExists.setStatut(StatutRendezVous.ANNULE);
        RendezVous rendezVous = rendezVousRepository.save(dejaExists);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    public Page<ResponseRendezVousDTO> getAllRendezVous(Pageable pageable){
        Page<RendezVous> page = rendezVousRepository.findAll(pageable);
        return page.map(rendezVousMapper::toResponseDTO);
    }

    public Page<ResponseRendezVousDTO> findRendezVousPatientByNom(String nom, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByPatient_Nom(nom, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    public Page<ResponseRendezVousDTO> findRendezVousMedecinByNom(String nom, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByMedecin_Nom(nom, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    public Page<ResponseRendezVousDTO> recuperDesRendezVousDeMedecinByDate(Long id, java.time.LocalDate date, Pageable pageable) {
        Page<RendezVous> rendezVous = rendezVousRepository.findByMedecin_IdAndDateRendezVous(id, date, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    public Page<ResponseRendezVousDTO> afficherRdvParStatut(StatutRendezVous statut, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByStatut(statut, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }
}
