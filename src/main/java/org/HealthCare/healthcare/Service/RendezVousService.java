package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.HealthCare.healthcare.Mapper.RendezVousMapper;
import org.HealthCare.healthcare.Repository.RendezVousRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendezVousService {
    private RendezVousRepository rendezVousRepository;
    private RendezVousMapper rendezVousMapper;

    public RendezVousService(RendezVousRepository rendezVousRepository , RendezVousMapper rendezVousMapper){
        this.rendezVousRepository = rendezVousRepository;
        this.rendezVousMapper = rendezVousMapper;
    }

    public ResponseRendezVousDTO createRendezVous(RequestRendezVousDTO dto){
        RendezVous rendezVousMapperEntity = rendezVousMapper.toEntity(dto);
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

    public ResponseRendezVousDTO annulerRendezVous(Long id , PutRendezVousDTO dto){
        RendezVous dejaExists = rendezVousRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Rendez Vous not found"));
        dejaExists.setStatut(dto.getStatut());
        RendezVous rendezVous = rendezVousRepository.save(dejaExists);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    public List<ResponseRendezVousDTO> getAllRendezVous(){
        return rendezVousRepository.findAll().stream().map(rendezVousMapper::toResponseDTO).toList();
    }

    public List<ResponseRendezVousDTO> findPatientByNom(String nom){
        List<RendezVous> rendezVous = rendezVousRepository.findByPatient_Nom(nom);
        return rendezVousMapper.toResponseDTOList(rendezVous);
    }

    public List<ResponseRendezVousDTO> findMedecinByNom(String nom){
        List<RendezVous> rendezVous = rendezVousRepository.findByMedecin_Nom(nom);
        return rendezVousMapper.toResponseDTOList(rendezVous);
    }
}
