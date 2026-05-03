
INSERT INTO patient (nom, prenom, telephone, date_naissance) VALUES
    ('Rida', 'Taki', 600000001, '2000-01-01'),
    ('Ali', 'Hassan', 600000002, '1998-05-10');

INSERT INTO medecin (nom, specialite, email, telephone) VALUES
    ('Dr Karim', 'Cardiologie', 'karim@gmail.com', 600000003),
    ('Dr Sara', 'Dermatologie', 'sara@gmail.com', 600000004);

INSERT INTO dossier_medical (diagnostic, observation, date_creation, patient_id) VALUES
    ('Diabete', 'Suivi régulier', CURDATE(), 1),
    ('Allergie', 'Eviter pollen', CURDATE(), 2);

INSERT INTO rendez_vous (date_rendez_vous, statut, patient_id, medecin_id) VALUES
    (CURDATE(), 'EN_ATTENTE', 1, 1),
    (CURDATE(), 'CONFIRME', 2, 2),
    (CURDATE(), 'ANNULE', 1, 2);