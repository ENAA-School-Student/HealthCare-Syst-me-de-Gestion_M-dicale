package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddDiagnosticDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddObservationDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedecalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Service.DossierMedicalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dossiers")
public class DossierMedecalController {
    private DossierMedicalService dossierMedicalService;

    public DossierMedecalController(DossierMedicalService dossierMedicalService){
        this.dossierMedicalService = dossierMedicalService;
    }

    @PostMapping
    public ResponseEntity<ResponseDossierMedicalDTO> createDossierMedical(@Valid @RequestBody RequestDossierMedecalDTO dto){
        ResponseDossierMedicalDTO responseDossierMedicalDTO = dossierMedicalService.createDossierMedical(dto);
        return new ResponseEntity<>(responseDossierMedicalDTO , HttpStatus.CREATED);
    }

    @PutMapping("/{id}/diagnostic")
    public ResponseEntity<ResponseDossierMedicalDTO> addDiagnostic(@PathVariable Long id , @Valid @RequestBody AddDiagnosticDTO dto){
        return ResponseEntity.ok(dossierMedicalService.addDiagnostic(id , dto));
    }

    @PutMapping("/{id}/observation")
    public ResponseEntity<ResponseDossierMedicalDTO> addObservation(@PathVariable Long id , @Valid @RequestBody AddObservationDTO dto){
        return ResponseEntity.ok(dossierMedicalService.addObservation(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDossierMedicalDTO> getDossierMedicalById(@PathVariable Long id){
        return ResponseEntity.ok(dossierMedicalService.getDossierById(id));
    }
}
