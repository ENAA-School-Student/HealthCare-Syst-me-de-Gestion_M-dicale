# 🏥 HealthCare+ : Système de Gestion Médicale

## 📝 Contexte du Projet

Dans le cadre de sa transformation numérique, une entreprise spécialisée en **HealthCare+** souhaite mettre en place une application de gestion de système médical afin de mieux organiser le suivi des patients, des médecins, des rendez-vous et des dossiers médicaux.

Cette application est une **API REST complète** répondant aux besoins fonctionnels du système, reposant sur une conception claire, une architecture professionnelle et l’utilisation de technologies backend modernes.

---

## 🛠️ Travail Demandé

### 🧩 Entités Principales

*   **Patient :** id, nom, prénom, email, téléphone, dateNaissance.
*   **Médecin :** id, nom, spécialité, email, téléphone.
*   **RendezVous :** id, dateRendezVous, statut.
*   **DossierMedical :** id, diagnostic, observation, dateCreation.
*   **User :** id, username, email, password, role.

### ⚙️ Fonctionnalités de Base

*   **Gestion des Patients :** Ajouter, modifier, supprimer, lister, consulter.
*   **Gestion des Médecins :** Ajouter, modifier, supprimer, lister.
*   **Gestion des Rendez-vous :** Créer, modifier, annuler, lister, rechercher par patient/médecin.
*   **Gestion Dossier Médical :** Créer dossier, ajouter diagnostic, ajouter observations, consulter dossier.

---

## 🔐 Sécurisation & Authentification (JWT)

L’objectif est de sécuriser l’accès à l’application via **Spring Security** et **JWT**.

### Fonctionnalités Attendues :
*   **Authentification :** Inscription, Connexion, Génération & Validation du token JWT, Gestion de l’expiration.
*   **Sécurisation API :** Protéger les endpoints, autoriser uniquement les utilisateurs authentifiés.
*   **Accès Libre :** Endpoints `/auth/login` et `/auth/register`.
*   **Gestion Utilisateur :** Entité `User` (id, username, email, password).
*   **Validation & Erreurs :** Validation des données, gestion globale des exceptions, messages d'erreurs personnalisés.

---

## 🚀 Fonctionnalités Avancées (V3)

### 📊 Pagination, Tri et Recherche
*   **Pagination :** Pour les Patients, Médecins, Rendez-vous, Dossiers médicaux.
*   **Tri :**
    *   Patients par nom.
    *   Médecins par spécialité.
    *   Rendez-vous par date.
*   **Recherche paginée :**
    *   Recherche patient par nom.
    *   Recherche médecin par spécialité.
    *   Recherche rendez-vous par statut.

### 🛡️ Gestion des Rôles & Permissions
*   **Rôles :** `ADMIN`, `MEDECIN`, `PATIENT`.
*   **ADMIN :** Gestion complète (Patients, Médecins, RDV, Dossiers, Utilisateurs).
*   **MEDECIN :** Consulter ses RDV, consulter dossiers médicaux, ajouter diagnostic, modifier observations.
*   **PATIENT :** Consulter son profil, ses RDV, son dossier médical, modifier certaines infos personnelles.

---

## 📦 Docker & Déploiement
L’application est conteneurisée pour faciliter le déploiement :
*   **Dockerfile**
*   **docker-compose.yml**

---

## 🏗️ Technologies Utilisées

*   **Backend :** Java 17 / 21, Spring Boot.
*   **Data :** Spring Data JPA, Hibernate, Flyway.
*   **Base de données :** SQL (MySQL/PostgreSQL), Derived Queries, `@Query` (SQL/JPQL).
*   **Architecture :** MVC, REST API, DTO & Mapper (MapStruct).
*   **Sécurité :** Spring Security, JWT, BCryptPasswordEncoder.
*   **Tests :** JUnit 5.
*   **Documentation :** Swagger / OpenAPI.
*   **DevOps :** Docker, Docker Compose, Git.

---

## 💡 Idée Hackathon Rally IA (Maroc)

### Problématiques Santé au Maroc
*   **Inégalités d’accès aux soins** entre zones urbaines et rurales.
*   **Délais de rendez-vous élevés** pour certaines spécialités.
*   **Parcours patient fragmenté** (données dispersées entre structures).
*   **Charge croissante des maladies chroniques** (diabète, HTA, etc.).
*   **Manque d’outils prédictifs** pour prioriser les patients à risque.

### Proposition : **SehhaFlow AI**
Une plateforme IA qui combine :
*   **Triage intelligent** (priorité médicale selon symptômes et historique).
*   **Orientation automatique** vers le bon niveau de soin (centre, spécialiste, téléconsultation).
*   **Prédiction de no-show** et optimisation des créneaux RDV.
*   **Score de risque chronique** pour renforcer la prévention.

### Pourquoi l’idée est logique et investissable
*   **Impact clair :** réduction des délais, meilleure prise en charge et suivi continu.
*   **Modèle économique hybride :** B2G (hôpitaux publics), B2B (cliniques/assureurs), B2B2C (patients).
*   **Scalabilité régionale :** solution réplicable en Afrique francophone.
*   **Attractivité investisseurs :**
    *   **Nationaux :** fonds healthtech, assureurs, groupes hospitaliers marocains.
    *   **Internationaux :** impact funds, healthtech VCs, institutions de développement.

---

## 📄 Conception UML
Le projet inclut :
1.  Diagramme de Classes
2.  Diagramme de Cas d'Utilisation
3.  Diagramme de Séquence
