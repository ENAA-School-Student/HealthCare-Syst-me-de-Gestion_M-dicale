ALTER TABLE dossier_medical ADD COLUMN patient_id BIGINT;
ALTER TABLE rendez_vous ADD COLUMN patient_id BIGINT;
ALTER TABLE rendez_vous ADD COLUMN medecin_id BIGINT;

ALTER TABLE dossier_medical ADD CONSTRAINT fk_dossierPatient
FOREIGN KEY (patient_id) REFERENCES patient(id),
ADD CONSTRAINT uk_dossierPatient UNIQUE (patient_id);

ALTER TABLE rendez_vous ADD CONSTRAINT fk_rendezVousPatient
FOREIGN KEY (patient_id) REFERENCES patient(id);

ALTER TABLE rendez_vous ADD CONSTRAINT fk_rendezVousMedecin
FOREIGN KEY (medecin_id) REFERENCES medecin(id);