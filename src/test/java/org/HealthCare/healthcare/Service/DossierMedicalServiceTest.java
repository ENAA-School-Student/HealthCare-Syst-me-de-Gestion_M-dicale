package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedecalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Entity.DossierMedical;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Mapper.DossierMedicalMapper;
import org.HealthCare.healthcare.Repository.DossierMedicalRepository;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DossierMedicalServiceTest {

    @Mock
    private DossierMedicalRepository dossierMedicalRepository;

    @Mock
    private DossierMedicalMapper dossierMedicalMapper;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private DossierMedicalService dossierMedicalService;

    @Test
    void createDossierMedical() {
        String observation = "test";
        RequestDossierMedecalDTO dossierMedecalDTO = new RequestDossierMedecalDTO();
        dossierMedecalDTO.setObservation(observation);
        dossierMedecalDTO.setPatientId(1L);

        Patient patient = new Patient();
        patient.setId(1L);

        DossierMedical dossierMedical = new DossierMedical();
        dossierMedical.setObservation(observation);
        dossierMedecalDTO.setPatientId(1L);

        ResponseDossierMedicalDTO responseDossierMedicalDTO = new ResponseDossierMedicalDTO();
        responseDossierMedicalDTO.setObservation(observation);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(dossierMedicalMapper.toEntity(dossierMedecalDTO)).thenReturn(dossierMedical);
        when(dossierMedicalRepository.save(dossierMedical)).thenReturn(dossierMedical);
        when(dossierMedicalMapper.toResponse(dossierMedical)).thenReturn(responseDossierMedicalDTO);

        ResponseDossierMedicalDTO responseDossierMedicalDTO1 = dossierMedicalService.createDossierMedical(dossierMedecalDTO);
        assertNotNull(dossierMedecalDTO);
        assertEquals(observation , responseDossierMedicalDTO1.getObservation());
    }

    @Test
    void getDossierById() {
        Long dossier_id = 1L;
        DossierMedical dossierMedical = new DossierMedical();
        dossierMedical.setId(dossier_id);

        ResponseDossierMedicalDTO responseDossierMedicalDTO = new ResponseDossierMedicalDTO();
        responseDossierMedicalDTO.setId(dossier_id);

        when(dossierMedicalRepository.findById(dossier_id)).thenReturn(Optional.of(dossierMedical));
        when(dossierMedicalMapper.toResponse(dossierMedical)).thenReturn(responseDossierMedicalDTO);

        ResponseDossierMedicalDTO responseDossierMedicalDTO1 = dossierMedicalService.getDossierById(dossier_id);

        assertNotNull(dossier_id);
        assertEquals(dossier_id , responseDossierMedicalDTO1.getId());
    }
}