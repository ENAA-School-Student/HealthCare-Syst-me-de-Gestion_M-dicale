package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.HealthCare.healthcare.Mapper.RendezVousMapper;
import org.HealthCare.healthcare.Repository.RendezVousRepository;
import org.HealthCare.healthcare.enums.StatutRendezVous;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RendezVousServiceTest {

    @Mock
    private RendezVousRepository rendezVousRepository;

    @Mock
    private RendezVousMapper rendezVousMapper;

    @InjectMocks
    private RendezVousService rendezVousService;

    @Test
    void annulerRendezVous() {
        Long id = 1L;
        RendezVous rendezVous = new RendezVous();
        rendezVous.setId(id);
        rendezVous.setStatut(StatutRendezVous.EN_ATTENTE);

        RendezVous rendezVous1 = new RendezVous();
        rendezVous1.setId(id);
        rendezVous1.setStatut(StatutRendezVous.ANNULE);

        ResponseRendezVousDTO responseRendezVousDTO = new ResponseRendezVousDTO();
        responseRendezVousDTO.setId(id);

        when(rendezVousRepository.findById(id)).thenReturn(Optional.of(rendezVous));
        when(rendezVousRepository.save(rendezVous)).thenReturn(rendezVous1);
        when(rendezVousMapper.toResponseDTO(rendezVous1)).thenReturn(responseRendezVousDTO);

        ResponseRendezVousDTO rendezVousDTO = rendezVousService.annulerRendezVous(id);

        assertNotNull(rendezVousDTO);
        assertEquals(StatutRendezVous.ANNULE , rendezVous.getStatut());
    }

    @Test
    void getAllRendezVous() {
        RendezVous rendezVous = new RendezVous();
        RendezVous rendezVous1 = new RendezVous();

        List<RendezVous> rendezVousList = List.of(rendezVous , rendezVous1);

        ResponseRendezVousDTO responseRendezVousDTO = new ResponseRendezVousDTO();
        responseRendezVousDTO.setId(1L);

        ResponseRendezVousDTO responseRendezVousDTO1 = new ResponseRendezVousDTO();
        responseRendezVousDTO1.setId(1L);

        when(rendezVousRepository.findAll()).thenReturn(rendezVousList);
        when(rendezVousMapper.toResponseDTO(rendezVous)).thenReturn(responseRendezVousDTO);
        when(rendezVousMapper.toResponseDTO(rendezVous1)).thenReturn(responseRendezVousDTO1);

        List<ResponseRendezVousDTO> rendezVousDTO = rendezVousService.getAllRendezVous();

        assertNotNull(rendezVousDTO);
        assertEquals(2 , rendezVousDTO.size());
    }

    @Test
    void findPatientByNom() {
        String nom = "rida";
        Patient patient = new Patient();
        RendezVous rendezVous = new RendezVous();
        patient.setNom(nom);
        rendezVous.setPatient(patient);

        List<RendezVous> list = List.of(rendezVous);

        ResponseRendezVousDTO responseRendezVousDTO = new ResponseRendezVousDTO();
        responseRendezVousDTO.setPatientId(1L);

        List<ResponseRendezVousDTO> rendezVousDTOList = List.of(responseRendezVousDTO);

        when(rendezVousRepository.findByPatient_Nom(nom)).thenReturn(list);
        when(rendezVousMapper.toResponseDTOList(list)).thenReturn(rendezVousDTOList);

        List<ResponseRendezVousDTO> responseRendezVousDTOS = rendezVousService.findRendezVousPatientByNom(nom);

        assertNotNull(responseRendezVousDTOS);
        assertEquals(1 , responseRendezVousDTOS.size());
        assertEquals(1 , responseRendezVousDTOS.get(0).getPatientId());
    }
}