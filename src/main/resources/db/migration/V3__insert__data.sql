-- Insertion de base pour les patients (SANS user_id car la colonne n'existe pas encore à ce stade)
INSERT INTO patient (nom, prenom, telephone, date_naissance) VALUES
    ('Rida', 'Taki', 600000001, '2000-01-01'),
    ('Ali', 'Hassan', 600000002, '1998-05-10'),
    ('Benani', 'Yassine', 600000005, '1995-03-15'),
    ('Zahiri', 'Laila', 600000006, '1992-07-20'),
    ('Amrani', 'Omar', 600000007, '1985-11-30'),
    ('Mansouri', 'Khadija', 600000008, '1990-01-25'),
    ('Chraibi', 'Anas', 600000009, '1988-09-12'),
    ('Alami', 'Salma', 600000010, '2002-05-05'),
    ('Idrissi', 'Mehdi', 600000011, '1997-12-01'),
    ('Fassi', 'Siham', 600000012, '1993-08-18'),
    ('Moussaoui', 'Hamza', 600000013, '1982-04-10'),
    ('Tazi', 'Nadia', 600000014, '1999-10-22');

-- Insertion de base pour les médecins
INSERT INTO medecin (nom, specialite, email, telephone) VALUES
    ('Dr Karim', 'Cardiologie', 'karim@test.com', 600000003),
    ('Dr Sara', 'Dermatologie', 'sara@test.com', 600000004),
    ('Dr Jalila', 'Pediatrie', 'jalila@gmail.com', 600000015),
    ('Dr Mourad', 'Ophtalmologie', 'mourad@gmail.com', 600000016),
    ('Dr Amina', 'Gynecologie', 'amina@gmail.com', 600000017),
    ('Dr Youssef', 'Cardiologie', 'youssef@gmail.com', 600000018);

-- Insertion des dossiers médicaux
INSERT INTO dossier_medical (diagnostic, observation, date_creation, patient_id) VALUES
    ('Diabete', 'Suivi régulier', CURDATE(), 1),
    ('Allergie', 'Eviter pollen', CURDATE(), 2),
    ('Hypertension', 'Régime sans sel', CURDATE(), 3),
    ('Anémie', 'Compléments fer', CURDATE(), 4),
    ('Asthme', 'Inhalateur si besoin', CURDATE(), 5);

-- Insertion des rendez-vous
INSERT INTO rendez_vous (date_rendez_vous, statut, patient_id, medecin_id) VALUES
    (CURDATE(), 'EN_ATTENTE', 1, 1),
    (CURDATE(), 'CONFIRME', 2, 2),
    (CURDATE(), 'ANNULE', 1, 2),
    ('2026-06-01', 'EN_ATTENTE', 3, 3),
    ('2026-06-02', 'CONFIRME', 4, 4),
    ('2026-06-03', 'EN_ATTENTE', 5, 5),
    ('2026-06-04', 'CONFIRME', 6, 1),
    ('2026-06-05', 'TERMINE', 7, 1),
    ('2026-06-06', 'EN_ATTENTE', 8, 2),
    ('2026-06-07', 'CONFIRME', 1, 3),
    ('2026-06-08', 'ANNULE', 2, 4),
    ('2026-06-09', 'EN_ATTENTE', 3, 2),
    ('2026-06-10', 'CONFIRME', 4, 1);
