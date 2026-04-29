package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.PutPatientDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.PutMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.ResponseMedecinDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.HealthCare.healthcare.Mapper.MedecinMapper;
import org.HealthCare.healthcare.Repository.MedecinRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MedecinServiceTest {

    @Mock
    private MedecinRepository medecinRepository;

    @Mock
    private MedecinMapper medecinMapper;

    @InjectMocks
    private MedecinService medecinService;

    @Test
    void updateMedecin() {
        Long id = 1L;
        String nom = "messi";
        PutMedecinDTO putMedecinDTO = new PutMedecinDTO();
        putMedecinDTO.setNom(nom);
        Medecin medecin = new Medecin();
        medecin.setNom(nom);
        ResponseMedecinDTO responseMedecinDTO = new ResponseMedecinDTO();
        responseMedecinDTO.setNom(nom);

        when(medecinRepository.findById(id)).thenReturn(medecin);
        when(medecinMapper.toEntity(putMedecinDTO)).thenReturn();
    }
}