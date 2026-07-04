package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.medecin.PutMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.RequestMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.ResponseMedecinDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.HealthCare.healthcare.Entity.User;
import org.HealthCare.healthcare.Mapper.MedecinMapper;
import org.HealthCare.healthcare.Repository.MedecinRepository;
import org.HealthCare.healthcare.Repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class MedecinService {
    private MedecinRepository medecinRepository;
    private MedecinMapper medecinMapper;
    private UserRepository userRepository;

    public MedecinService(MedecinRepository medecinRepository , MedecinMapper medecinMapper, UserRepository userRepository){
        this.medecinRepository = medecinRepository;
        this.medecinMapper = medecinMapper;
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "medecins", allEntries = true)
    public ResponseMedecinDTO addMedecin(RequestMedecinDTO dto){
        Medecin medecinMapperEntity = medecinMapper.toEntity(dto);
        Medecin medecin = medecinRepository.save(medecinMapperEntity);
        return medecinMapper.toResponseDTO(medecin);
    }

    @CacheEvict(value = "medecins", allEntries = true)
    public ResponseMedecinDTO updateMedecin(Long id , PutMedecinDTO dto){
        Medecin medecinDejaExists = medecinRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Medecin introuvable"));
        medecinMapper.updateMedecin(dto , medecinDejaExists);
        Medecin medecin = medecinRepository.save(medecinDejaExists);
        return medecinMapper.toResponseDTO(medecin);
    }

    @CacheEvict(value = "medecins", allEntries = true)
    public void deleteMedecin(Long id){
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medecin introuvable"));
        User user = medecin.getUser();
        medecinRepository.deleteById(id);
        if (user != null) {
            userRepository.deleteById(user.getId());
        }
    }

    @Cacheable(value = "medecins", key = "#id")
    public ResponseMedecinDTO getMedecinById(Long id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medecin introuvable"));
        return medecinMapper.toResponseDTO(medecin);
    }

    @Cacheable(value = "medecins", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ResponseMedecinDTO> getAllMedecin(Pageable pageable){
        Page<Medecin> page = medecinRepository.findAll(pageable);
                return page.map(medecinMapper::toResponseDTO);

    }

    @Cacheable(value = "medecins", key = "#tele + '-' + #pageable.pageNumber")
    public Page<ResponseMedecinDTO> getMedecinByTele(Long tele , Pageable pageable){
        Page<Medecin> page = medecinRepository.findMedecinByTelephone(tele , pageable);
        return page.map(medecinMapper::toResponseDTO);
    }

    @Cacheable(value = "medecins", key = "#specialite + '-' + #pageable.pageNumber")
    public Page<ResponseMedecinDTO> searchMedecinBySpecialite(String specialite, Pageable pageable) {
        Page<Medecin> medecins = medecinRepository.findBySpecialiteContaining(specialite, pageable);
        return medecins.map(medecinMapper::toResponseDTO);
    }
}
