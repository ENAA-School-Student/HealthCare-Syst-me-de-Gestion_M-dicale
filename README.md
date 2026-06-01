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

## 📄 Conception UML
Le projet inclut :
1.  Diagramme de Classes
2.  Diagramme de Cas d'Utilisation
3.  Diagramme de Séquence
