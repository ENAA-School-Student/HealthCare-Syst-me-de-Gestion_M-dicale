package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.PutPatientDTO;
import org.HealthCare.healthcare.DTO.patient.RequestPatientDTO;
import org.HealthCare.healthcare.DTO.patient.ResponsePatientDTO;
import org.HealthCare.healthcare.Service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponsePatientDTO> addPatient(@Valid @RequestBody RequestPatientDTO dto){
        ResponsePatientDTO patientDTO = patientService.addPatient(dto);
        return new ResponseEntity<>(patientDTO ,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.id == @patientService.getPatientById(#id).userId)")
    public ResponseEntity<ResponsePatientDTO> updatePatient(@PathVariable Long id , @Valid @RequestBody PutPatientDTO dto){
        return ResponseEntity.ok(patientService.updatePatient(id , dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponsePatientDTO>> getAllPatients(@PageableDefault(sort = "nom") Pageable pageable){
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.id == @patientService.getPatientById(#id).userId)")
    public ResponseEntity<ResponsePatientDTO> getPatientById(@PathVariable Long id){
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponsePatientDTO>> searchPatientByNom(@RequestParam String nom, @PageableDefault(sort = "nom") Pageable pageable){
        return ResponseEntity.ok(patientService.searchPatientByNom(nom, pageable));
    }
}
