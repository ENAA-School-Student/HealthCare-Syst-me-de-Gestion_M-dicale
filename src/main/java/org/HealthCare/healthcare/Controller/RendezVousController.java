package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Service.RendezVousService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseRendezVousDTO> createRendezVous(@Valid @RequestBody RequestRendezVousDTO dto){
        ResponseRendezVousDTO responseRendezVousDTO = rendezVousService.createRendezVous(dto);
        return new ResponseEntity<>(responseRendezVousDTO , HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRendezVousDTO> updateRendezVous(@PathVariable Long id , @Valid @RequestBody PutRendezVousDTO dto){
        return ResponseEntity.ok(rendezVousService.updateRendezVous(id, dto));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<ResponseRendezVousDTO> annulerRendezVous(@PathVariable Long id){
        return ResponseEntity.ok(rendezVousService.annulerRendezVous(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseRendezVousDTO>> getAllRendezVous(){
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

    @GetMapping("/patient/{nom}")
    public ResponseEntity<List<ResponseRendezVousDTO>> getRendezVousPatientByNom(@PathVariable String nom){
        return ResponseEntity.ok(rendezVousService.findRendezVousPatientByNom(nom));
    }

    @GetMapping("/medecin/{nom}")
    public ResponseEntity<List<ResponseRendezVousDTO>> getRendezVousMedecinByNom(@PathVariable String nom){
        return ResponseEntity.ok(rendezVousService.findRendezVousMedecinByNom(nom));
    }
}
