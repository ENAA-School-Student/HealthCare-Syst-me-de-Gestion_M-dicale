package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Service.RendezVousService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {
    private RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService){
        this.rendezVousService = rendezVousService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ResponseRendezVousDTO> createRendezVous(@Valid @RequestBody RequestRendezVousDTO dto){
        ResponseRendezVousDTO responseRendezVousDTO = rendezVousService.createRendezVous(dto);
        return new ResponseEntity<>(responseRendezVousDTO , HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ResponseRendezVousDTO> updateRendezVous(@PathVariable Long id , @Valid @RequestBody PutRendezVousDTO dto){
        return ResponseEntity.ok(rendezVousService.updateRendezVous(id, dto));
    }

    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ResponseRendezVousDTO> annulerRendezVous(@PathVariable Long id){
        return ResponseEntity.ok(rendezVousService.annulerRendezVous(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getAllRendezVous(Pageable pageable){
        return ResponseEntity.ok(rendezVousService.getAllRendezVous(pageable));
    }

    @GetMapping("/patient/{nom}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseRendezVousDTO>> getRendezVousPatientByNom(@PathVariable String nom){
        return ResponseEntity.ok(rendezVousService.findRendezVousPatientByNom(nom));
    }

    @GetMapping("/medecin/{nom}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseRendezVousDTO>> getRendezVousMedecinByNom(@PathVariable String nom){
        return ResponseEntity.ok(rendezVousService.findRendezVousMedecinByNom(nom));
    }
}
