package org.HealthCare.healthcare.Service;

import org.HealthCare.healthcare.Mapper.PatientMapper;
import org.HealthCare.healthcare.Repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Service
public class PatientService {
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository , PatientMapper patientMapper){
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }


}
