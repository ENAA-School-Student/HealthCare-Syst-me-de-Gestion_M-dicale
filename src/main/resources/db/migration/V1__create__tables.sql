CREATE TABLE patient(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    telephone BIGINT,
    date_naissance DATE
);
CREATE TABLE medecin(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    specialite VARCHAR(255),
    email VARCHAR(255),
    telephone BIGINT
);
CREATE TABLE dossier_medical(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diagnostic VARCHAR(255),
    observation VARCHAR(255),
    date_creation DATE
);
CREATE TABLE rendez_vous (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_rendez_vous DATE,
    statut ENUM('EN_ATTENTE' , 'CONFIRME' , 'ANNULE' , 'TERMINE')
);