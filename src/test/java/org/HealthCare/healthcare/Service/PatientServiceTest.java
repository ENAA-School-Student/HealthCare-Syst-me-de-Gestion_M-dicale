package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.RequestPatientDTO;
import org.HealthCare.healthcare.DTO.patient.ResponsePatientDTO;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Mapper.PatientMapper;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    @Test
    void addPatient() {
        String nom = "rida";
        RequestPatientDTO patientDTO = new RequestPatientDTO();
        patientDTO.setNom(nom);
        Patient patient = new Patient();
        patient.setNom(nom);
        ResponsePatientDTO responsePatientDTO = new ResponsePatientDTO();
        responsePatientDTO.setNom(nom);

        when(patientMapper.toEntity(patientDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponseDTO(patient)).thenReturn(responsePatientDTO);

        ResponsePatientDTO patientDTO1 = patientService.addPatient(patientDTO);
        assertNotNull(patientDTO1);
        assertEquals("rida" , patientDTO1.getNom());
    }
}