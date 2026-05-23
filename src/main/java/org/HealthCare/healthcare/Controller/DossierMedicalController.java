package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddDiagnosticDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.AddObservationDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.RequestDossierMedicalDTO;
import org.HealthCare.healthcare.DTO.patient.dossierMedical.ResponseDossierMedicalDTO;
import org.HealthCare.healthcare.Service.DossierMedicalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dossiers")
public class DossierMedicalController {
    private DossierMedicalService dossierMedicalService;

    public DossierMedicalController(DossierMedicalService dossierMedicalService){
        this.dossierMedicalService = dossierMedicalService;
    }

    @PostMapping
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<ResponseDossierMedicalDTO> createDossierMedical(@Valid @RequestBody RequestDossierMedicalDTO dto){
        ResponseDossierMedicalDTO responseDossierMedicalDTO = dossierMedicalService.createDossierMedical(dto);
        return new ResponseEntity<>(responseDossierMedicalDTO , HttpStatus.CREATED);
    }

    @PutMapping("/{id}/diagnostic")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<ResponseDossierMedicalDTO> addDiagnostic(@PathVariable Long id , @Valid @RequestBody AddDiagnosticDTO dto){
        return ResponseEntity.ok(dossierMedicalService.addDiagnostic(id , dto));
    }

    @PutMapping("/{id}/observation")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<ResponseDossierMedicalDTO> addObservation(@PathVariable Long id , @Valid @RequestBody AddObservationDTO dto){
        return ResponseEntity.ok(dossierMedicalService.addObservation(id, dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEDECIN')")
    public ResponseEntity<ResponseDossierMedicalDTO> getDossierMedicalById(@PathVariable Long id){
        return ResponseEntity.ok(dossierMedicalService.getDossierById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseDossierMedicalDTO>> getAllDossierMedical(@PageableDefault(sort = "dateCreation", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(dossierMedicalService.getAllDossierMedical(pageable));
    }
}
