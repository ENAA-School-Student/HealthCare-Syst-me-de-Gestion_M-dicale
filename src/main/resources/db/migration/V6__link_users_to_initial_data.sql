-- Insertion des Utilisateurs (Passwords: password123)
INSERT INTO users (username, email, password, role) VALUES
    ('rida', 'rida@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu', 'PATIENT'),
    ('ali', 'ali@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu', 'PATIENT'),
    ('karim', 'karim@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu', 'MEDECIN'),
    ('sara', 'sara@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu', 'MEDECIN'),
    ('yassine', 'yassine@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu', 'PATIENT'),
    ('admin', 'admin@test.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOnu', 'ADMIN');

-- Mise à jour des Patients pour les lier aux Users
UPDATE patient SET user_id = 1 WHERE id = 1;
UPDATE patient SET user_id = 2 WHERE id = 2;
UPDATE patient SET user_id = 5 WHERE id = 3;

-- Mise à jour des Médecins pour les lier aux Users
UPDATE medecin SET user_id = 3 WHERE id = 1;
UPDATE medecin SET user_id = 4 WHERE id = 2;
