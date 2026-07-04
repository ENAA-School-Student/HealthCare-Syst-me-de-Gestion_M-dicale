package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.DTO.patient.rendezVous.PutRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.RequestRendezVousDTO;
import org.HealthCare.healthcare.DTO.patient.rendezVous.ResponseRendezVousDTO;
import org.HealthCare.healthcare.Entity.Medecin;
import org.HealthCare.healthcare.Entity.Patient;
import org.HealthCare.healthcare.Entity.RendezVous;
import org.HealthCare.healthcare.Mapper.RendezVousMapper;
import org.HealthCare.healthcare.Repository.MedecinRepository;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.HealthCare.healthcare.Repository.RendezVousRepository;
import org.HealthCare.healthcare.enums.StatutRendezVous;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendezVousService {
    private RendezVousRepository rendezVousRepository;
    private RendezVousMapper rendezVousMapper;
    private PatientRepository patientRepository;
    private MedecinRepository medecinRepository;
    private PdfGeneratorService pdfGeneratorService;

    public RendezVousService(RendezVousRepository rendezVousRepository , RendezVousMapper rendezVousMapper , PatientRepository patientRepository , MedecinRepository medecinRepository, PdfGeneratorService pdfGeneratorService){
        this.rendezVousRepository = rendezVousRepository;
        this.rendezVousMapper = rendezVousMapper;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public byte[] exportRendezVousByPatientToPdf(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        List<RendezVous> rendezVousList = rendezVousRepository.findByPatient_Id(patientId);
        return pdfGeneratorService.generateRendezVousListPdf(patient, rendezVousList);
    }

    @CacheEvict(value = "rendezvous", allEntries = true)
    public ResponseRendezVousDTO createRendezVous(RequestRendezVousDTO dto){
        Patient patient = patientRepository.findById(dto.getPatientId()).
                orElseThrow(()-> new RuntimeException("Patient not found"));
        Medecin medecin = medecinRepository.findById(dto.getMedecinId()).
                orElseThrow(()-> new RuntimeException("Medecin not found"));
        RendezVous rendezVousMapperEntity = rendezVousMapper.toEntity(dto);
        rendezVousMapperEntity.setPatient(patient);
        rendezVousMapperEntity.setMedecin(medecin);
        RendezVous rendezVous = rendezVousRepository.save(rendezVousMapperEntity);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    @CacheEvict(value = "rendezvous", allEntries = true)
    public ResponseRendezVousDTO updateRendezVous(Long id , PutRendezVousDTO dto){
        RendezVous dejaExists = rendezVousRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Rendez Vous not found"));
        rendezVousMapper.updateRendeVous(dto , dejaExists);
        RendezVous rendezVous = rendezVousRepository.save(dejaExists);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    @CacheEvict(value = "rendezvous", allEntries = true)
    public ResponseRendezVousDTO annulerRendezVous(Long id){
        RendezVous dejaExists = rendezVousRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Rendez Vous not found"));
        dejaExists.setStatut(StatutRendezVous.ANNULE);
        RendezVous rendezVous = rendezVousRepository.save(dejaExists);
        return rendezVousMapper.toResponseDTO(rendezVous);
    }

    @Cacheable(value = "rendezvous", key = "#id")
    public ResponseRendezVousDTO getRendezVousById(Long id) {
        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rendez Vous not found"));
        return rendezVousMapper.toResponseDTO(rdv);
    }

    @Cacheable(value = "rendezvous", key = "'all-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> getAllRendezVous(Pageable pageable){
        Page<RendezVous> page = rendezVousRepository.findAll(pageable);
        return page.map(rendezVousMapper::toResponseDTO);
    }

    @Cacheable(value = "rendezvous", key = "'patient-' + #nom + '-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> findRendezVousPatientByNom(String nom, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByPatient_Nom(nom, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    @Cacheable(value = "rendezvous", key = "'medecin-' + #nom + '-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> findRendezVousMedecinByNom(String nom, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByMedecin_Nom(nom, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    @Cacheable(value = "rendezvous", key = "'medecin-date-' + #id + '-' + #date + '-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> recuperDesRendezVousDeMedecinByDate(Long id, java.time.LocalDate date, Pageable pageable) {
        Page<RendezVous> rendezVous = rendezVousRepository.findByMedecin_IdAndDateRendezVous(id, date, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    @Cacheable(value = "rendezvous", key = "'statut-' + #statut + '-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> afficherRdvParStatut(StatutRendezVous statut, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByStatut(statut, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    @Cacheable(value = "rendezvous", key = "'patient-id-' + #id + '-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> findRendezVousPatientById(Long id, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByPatient_Id(id, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }

    @Cacheable(value = "rendezvous", key = "'medecin-id-' + #id + '-' + #pageable.pageNumber")
    public Page<ResponseRendezVousDTO> findRendezVousMedecinById(Long id, Pageable pageable){
        Page<RendezVous> rendezVous = rendezVousRepository.findByMedecin_Id(id, pageable);
        return rendezVous.map(rendezVousMapper::toResponseDTO);
    }
}
