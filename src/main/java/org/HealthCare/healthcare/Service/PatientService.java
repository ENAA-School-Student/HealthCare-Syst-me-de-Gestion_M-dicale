package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.PutPatientDTO;
import org.HealthCare.healthcare.DTO.patient.RequestPatientDTO;
import org.HealthCare.healthcare.DTO.patient.ResponsePatientDTO;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Mapper.PatientMapper;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository , PatientMapper patientMapper){
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @CacheEvict(value = "patients", allEntries = true)
    public ResponsePatientDTO addPatient(RequestPatientDTO dto){
        Patient patientMapperEntity = patientMapper.toEntity(dto);
        Patient patient = patientRepository.save(patientMapperEntity);
        return patientMapper.toResponseDTO(patient);
    }

    @CacheEvict(value = "patients", allEntries = true)
    public ResponsePatientDTO updatePatient(Long id , PutPatientDTO dto){
        Patient patientDejaExists = patientRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Patient introuvable"));
        patientMapper.updatePatient(dto , patientDejaExists);
        Patient patient = patientRepository.save(patientDejaExists);
        return patientMapper.toResponseDTO(patient);
    }

    @CacheEvict(value = "patients", allEntries = true)
    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }

    @Cacheable(value = "patients", key = "'all-' + #pageable.pageNumber")
    public Page<ResponsePatientDTO> getAllPatients(Pageable pageable) {
           Page < Patient > page = patientRepository.findAll(pageable);
           return page.map(patientMapper::toResponseDTO);
    }

    @Cacheable(value = "patients", key = "#id")
    public ResponsePatientDTO getPatientById(Long id){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new RuntimeException("Patient introuvable"));
        return patientMapper.toResponseDTO(patient);

    }

    @Cacheable(value = "patients", key = "'search-' + #nom + '-' + #pageable.pageNumber")
    public Page<ResponsePatientDTO> searchPatientByNom(String nom, Pageable pageable) {
        Page<Patient> patients = patientRepository.recuperPatientbyNom(nom, pageable);
        return patients.map(patientMapper::toResponseDTO);
    }
}
