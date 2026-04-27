package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.PutPatientDTO;
import org.HealthCare.healthcare.DTO.patient.RequestPatientDTO;
import org.HealthCare.healthcare.DTO.patient.ResponsePatientDTO;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Mapper.PatientMapper;
import org.HealthCare.healthcare.Repository.PatientRepository;
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

    public ResponsePatientDTO addPatient(RequestPatientDTO dto){
        Patient patientMapperEntity = patientMapper.toEntity(dto);
        Patient patient = patientRepository.save(patientMapperEntity);
        return patientMapper.toResponseDTO(patient);
    }

    public ResponsePatientDTO updatePatientm(Long id , PutPatientDTO dto){
        Patient patientDejaExists = patientRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Patient introuvable"));
        patientMapper.updatePatient(dto , patientDejaExists);
        Patient patient = patientRepository.save(patientDejaExists);
        return patientMapper.toResponseDTO(patient);
    }

    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }

    public List<ResponsePatientDTO> getAllPatient(){
        return patientRepository.findAll().stream().map(patientMapper::toResponseDTO)
                .toList();
    }

    public ResponsePatientDTO getPatientById(Long id){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new RuntimeException("Patient introuvable"));
        return patientMapper.toResponseDTO(patient);

    }
}
