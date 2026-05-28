package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.medecin.PutMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.RequestMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.ResponseMedecinDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.HealthCare.healthcare.Mapper.MedecinMapper;
import org.HealthCare.healthcare.Repository.MedecinRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class MedecinService {
    private MedecinRepository medecinRepository;
    private MedecinMapper medecinMapper;

    public MedecinService(MedecinRepository medecinRepository , MedecinMapper medecinMapper){
        this.medecinRepository = medecinRepository;
        this.medecinMapper = medecinMapper;
    }

    public ResponseMedecinDTO addMedecin(RequestMedecinDTO dto){
        Medecin medecinMapperEntity = medecinMapper.toEntity(dto);
        Medecin medecin = medecinRepository.save(medecinMapperEntity);
        return medecinMapper.toResponseDTO(medecin);
    }

    public ResponseMedecinDTO updateMedecin(Long id , PutMedecinDTO dto){
        Medecin medecinDejaExists = medecinRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Medecin introuvable"));
        medecinMapper.updateMedecin(dto , medecinDejaExists);
        Medecin medecin = medecinRepository.save(medecinDejaExists);
        return medecinMapper.toResponseDTO(medecin);
    }

    public void deleteMedecin(Long id){
        medecinRepository.deleteById(id);
    }

    public ResponseMedecinDTO getMedecinById(Long id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medecin introuvable"));
        return medecinMapper.toResponseDTO(medecin);
    }

    public Page<ResponseMedecinDTO> getAllMedecin(Pageable pageable){
        Page<Medecin> page = medecinRepository.findAll(pageable);
                return page.map(medecinMapper::toResponseDTO);

    }

    public Page<ResponseMedecinDTO> searchMedecinBySpecialite(String specialite, Pageable pageable) {
        Page<Medecin> medecins = medecinRepository.findBySpecialiteContaining(specialite, pageable);
        return medecins.map(medecinMapper::toResponseDTO);
    }
}
