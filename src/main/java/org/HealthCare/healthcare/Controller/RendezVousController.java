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
    public ResponseEntity<Page<ResponseRendezVousDTO>> getAllRendezVous(@PageableDefault(sort = "dateRendezVous") Pageable pageable){
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
