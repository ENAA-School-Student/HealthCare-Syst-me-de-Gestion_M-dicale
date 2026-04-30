package org.HealthCare.healthcare.Service;

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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


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
        String Newnom = "messi";
        PutMedecinDTO putMedecinDTO = new PutMedecinDTO();
        putMedecinDTO.setNom(Newnom);
        Medecin medecin = new Medecin();
        medecin.setId(id);
        medecin.setNom("rida");
        ResponseMedecinDTO responseMedecinDTO = new ResponseMedecinDTO();
        responseMedecinDTO.setNom(Newnom);

        when(medecinRepository.findById(id)).thenReturn(Optional.of(medecin));
        when(medecinRepository.save(medecin)).thenReturn(medecin);
        when(medecinMapper.toResponseDTO(medecin)).thenReturn(responseMedecinDTO);

        ResponseMedecinDTO medecinDTO = medecinService.updateMedecin(id , putMedecinDTO);
        assertNotNull(medecinDTO);
        assertEquals(Newnom, medecinDTO.getNom());
    }
}