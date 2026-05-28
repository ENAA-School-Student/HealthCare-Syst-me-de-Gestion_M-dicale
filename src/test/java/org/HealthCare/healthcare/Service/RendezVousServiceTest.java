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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        Pageable pageable = PageRequest.of(0, 10);
        Page<RendezVous> page = new PageImpl<>(rendezVousList, pageable, rendezVousList.size());

        ResponseRendezVousDTO responseRendezVousDTO = new ResponseRendezVousDTO();
        responseRendezVousDTO.setId(1L);

        ResponseRendezVousDTO responseRendezVousDTO1 = new ResponseRendezVousDTO();
        responseRendezVousDTO1.setId(1L);

        when(rendezVousRepository.findAll(pageable)).thenReturn(page);
        when(rendezVousMapper.toResponseDTO(rendezVous)).thenReturn(responseRendezVousDTO);
        when(rendezVousMapper.toResponseDTO(rendezVous1)).thenReturn(responseRendezVousDTO1);

        Page<ResponseRendezVousDTO> rendezVousDTO = rendezVousService.getAllRendezVous(pageable);

        assertNotNull(rendezVousDTO);
        assertEquals(2 , rendezVousDTO.getContent().size());
    }

    @Test
    void findPatientByNom() {
        String nom = "rida";
        Patient patient = new Patient();
        RendezVous rendezVous = new RendezVous();
        patient.setNom(nom);
        rendezVous.setPatient(patient);

        Pageable pageable = PageRequest.of(0, 10);
        List<RendezVous> list = List.of(rendezVous);
        Page<RendezVous> page = new PageImpl<>(list, pageable, list.size());

        ResponseRendezVousDTO responseRendezVousDTO = new ResponseRendezVousDTO();
        responseRendezVousDTO.setPatientId(1L);

        when(rendezVousRepository.findByPatient_Nom(nom, pageable)).thenReturn(page);
        when(rendezVousMapper.toResponseDTO(rendezVous)).thenReturn(responseRendezVousDTO);

        Page<ResponseRendezVousDTO> responseRendezVousDTOS = rendezVousService.findRendezVousPatientByNom(nom, pageable);

        assertNotNull(responseRendezVousDTOS);
        assertEquals(1 , responseRendezVousDTOS.getContent().size());
        assertEquals(1L , responseRendezVousDTOS.getContent().get(0).getPatientId());
    }
}
