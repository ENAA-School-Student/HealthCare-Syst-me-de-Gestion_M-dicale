package org.HealthCare.healthcare.Controller;

import jakarta.validation.Valid;
import org.HealthCare.healthcare.DTO.patient.medecin.PutMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.RequestMedecinDTO;
import org.HealthCare.healthcare.DTO.patient.medecin.ResponseMedecinDTO;
import org.HealthCare.healthcare.Service.MedecinService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medecins")
public class MedecinController {
    private MedecinService medecinService;

    public MedecinController(MedecinService medecinService){
        this.medecinService = medecinService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMedecinDTO> addMedecin(@Valid @RequestBody RequestMedecinDTO dto){
        ResponseMedecinDTO responseMedecinDTO = medecinService.addMedecin(dto);
        return new ResponseEntity<>(responseMedecinDTO ,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MEDECIN') and principal.id == @medecinService.getMedecinById(#id).userId)")
    public ResponseEntity<ResponseMedecinDTO> updateMedecin(@PathVariable Long id , @Valid @RequestBody PutMedecinDTO dto){
        return ResponseEntity.ok(medecinService.updateMedecin(id , dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long id){
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MEDECIN') and principal.id == @medecinService.getMedecinById(#id).userId)")
    public ResponseEntity<ResponseMedecinDTO> getMedecinById(@PathVariable Long id){
        return ResponseEntity.ok(medecinService.getMedecinById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseMedecinDTO>> getAllMedecin(@PageableDefault(sort = "specialite") Pageable pageable){
        return ResponseEntity.ok(medecinService.getAllMedecin(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ResponseMedecinDTO>> searchMedecinBySpecialite(@RequestParam String specialite, @PageableDefault(sort = "specialite") Pageable pageable){
        return ResponseEntity.ok(medecinService.searchMedecinBySpecialite(specialite, pageable));
    }

}
