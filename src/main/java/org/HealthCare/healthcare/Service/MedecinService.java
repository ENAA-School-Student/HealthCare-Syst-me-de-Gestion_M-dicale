package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.ResponsePatientDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.PutMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.RequestMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.ResponseMedecinDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.HealthCare.healthcare.Mapper.MedecinMapper;
import org.HealthCare.healthcare.Repository.MedecinRepository;
import org.springframework.stereotype.Service;

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

    public List<ResponseMedecinDTO> getAllMedecin(){
        return medecinRepository.findAll().stream().map(medecinMapper::toResponseDTO).
                toList();
    }
}
