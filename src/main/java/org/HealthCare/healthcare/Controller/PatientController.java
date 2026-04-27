package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.PutPatientDTO;
import org.HealthCare.healthcare.DTO.patient.RequestPatientDTO;
import org.HealthCare.healthcare.DTO.patient.ResponsePatientDTO;
import org.HealthCare.healthcare.Service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponsePatientDTO> addPatient(@Valid @RequestBody RequestPatientDTO dto){
        ResponsePatientDTO patientDTO = patientService.addPatient(dto);
        return new ResponseEntity<>(patientDTO ,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePatientDTO> updatePatient(@PathVariable Long id , @Valid @RequestBody PutPatientDTO dto){
        return ResponseEntity.ok(patientService.updatePatient(id , dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ResponsePatientDTO>> getAllPatients(){
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePatientDTO> getPatientById(@PathVariable Long id){
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
}
