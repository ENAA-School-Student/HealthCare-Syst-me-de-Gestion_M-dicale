package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Service.RendezVousService;
import org.HealthCare.healthcare.Entity.User;
import org.HealthCare.healthcare.enums.RoleUser;
import org.HealthCare.healthcare.enums.StatutRendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {
    private RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService){
        this.rendezVousService = rendezVousService;
    }

    @GetMapping("/patient/{patientId}/download")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.id == @patientService.getPatientById(#patientId).userId)")
    public ResponseEntity<byte[]> downloadRendezVousPdf(@PathVariable Long patientId) {
        byte[] pdf = rendezVousService.exportRendezVousByPatientToPdf(patientId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "rendez_vous_patient_" + patientId + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.id == @patientService.getPatientById(#dto.patientId).userId)")
    public ResponseEntity<ResponseRendezVousDTO> createRendezVous(@Valid @RequestBody RequestRendezVousDTO dto){
        ResponseRendezVousDTO responseRendezVousDTO = rendezVousService.createRendezVous(dto);
        return new ResponseEntity<>(responseRendezVousDTO , HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'MEDECIN')")
    public ResponseEntity<ResponseRendezVousDTO> getRendezVousById(@PathVariable Long id){
        return ResponseEntity.ok(rendezVousService.getRendezVousById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.id == @patientService.getPatientById(@rendezVousService.getRendezVousById(#id).patientId).userId)")
    public ResponseEntity<ResponseRendezVousDTO> updateRendezVous(@PathVariable Long id , @Valid @RequestBody PutRendezVousDTO dto){
        return ResponseEntity.ok(rendezVousService.updateRendezVous(id, dto));
    }

    @PutMapping("/{id}/annuler")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.id == @patientService.getPatientById(@rendezVousService.getRendezVousById(#id).patientId).userId)")
    public ResponseEntity<ResponseRendezVousDTO> annulerRendezVous(@PathVariable Long id){
        return ResponseEntity.ok(rendezVousService.annulerRendezVous(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getAllRendezVous(Pageable pageable){
        return ResponseEntity.ok(rendezVousService.getAllRendezVous(pageable));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('PATIENT', 'MEDECIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getMyRendezVous(@AuthenticationPrincipal User user, Pageable pageable) {
        if (user.getRole() == RoleUser.PATIENT && user.getPatient() != null) {
            return ResponseEntity.ok(rendezVousService.findRendezVousPatientById(user.getPatient().getId(), pageable));
        } else if (user.getRole() == RoleUser.MEDECIN && user.getMedecin() != null) {
            return ResponseEntity.ok(rendezVousService.findRendezVousMedecinById(user.getMedecin().getId(), pageable));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/patient/{nom}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getRendezVousPatientByNom(@PathVariable String nom, Pageable pageable){
        return ResponseEntity.ok(rendezVousService.findRendezVousPatientByNom(nom, pageable));
    }

    @GetMapping("/medecin/{nom}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getRendezVousMedecinByNom(@PathVariable String nom, Pageable pageable){
        return ResponseEntity.ok(rendezVousService.findRendezVousMedecinByNom(nom, pageable));
    }

    @GetMapping("/medecin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getRendezVousDeMedecin
            (@RequestParam Long id , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date, Pageable pageable){
        return ResponseEntity.ok(rendezVousService.recuperDesRendezVousDeMedecinByDate(id , date, pageable));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseRendezVousDTO>> getRendezVousByStatut(@PathVariable StatutRendezVous statut, @PageableDefault(sort = "dateRendezVous") Pageable pageable){
        return ResponseEntity.ok(rendezVousService.afficherRdvParStatut(statut, pageable));
    }

}
