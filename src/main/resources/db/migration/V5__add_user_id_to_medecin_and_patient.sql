ALTER TABLE medecin ADD COLUMN user_id BIGINT;
ALTER TABLE patient ADD COLUMN user_id BIGINT;

ALTER TABLE medecin ADD CONSTRAINT fk_medecinUser
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE patient ADD CONSTRAINT fk_patientUser
FOREIGN KEY (user_id) REFERENCES users(id);
