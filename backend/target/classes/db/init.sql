-- =============================================================
-- Patient Portal — Script de création de la base de données MySQL
-- =============================================================

CREATE DATABASE IF NOT EXISTS patientportal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE patientportal;

-- =============================================================
-- TABLE : users
-- Stratégie SINGLE_TABLE : patients, doctors et admins sont
-- tous dans cette table. La colonne `role` distingue les types.
-- =============================================================
CREATE TABLE IF NOT EXISTS users (
    id                    INT          NOT NULL AUTO_INCREMENT,
    role                  VARCHAR(20)  NOT NULL,          -- 'patient' | 'doctor' | 'admin'
    username              VARCHAR(100) NOT NULL UNIQUE,
    password              VARCHAR(255) NOT NULL,
    first_name            VARCHAR(100),
    last_name             VARCHAR(100),
    name                  VARCHAR(200),
    email                 VARCHAR(200) NOT NULL UNIQUE,
    last_login            DATETIME,
    last_password_change  DATETIME,

    -- Colonnes spécifiques à chaque rôle (NULL pour les autres rôles)
    patient_id            INT,
    doctor_id             INT,
    admin_id              INT,

    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================
-- TABLE : appointments
-- Un rendez-vous lie un patient (FK) et un doctor (FK).
-- =============================================================
CREATE TABLE IF NOT EXISTS appointments (
    id            INT          NOT NULL AUTO_INCREMENT,
    patient_id    INT          NOT NULL,
    doctor_id     INT          NOT NULL,
    start_time    DATETIME     NOT NULL,
    end_time      DATETIME,
    last_updated  DATETIME,
    status        VARCHAR(20)  NOT NULL DEFAULT 'UNSPECIFIED',  -- ACTIVE | CANCELLED | UNSPECIFIED

    PRIMARY KEY (id),
    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)  REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================
-- Données de test (optionnel — supprimer en production)
-- =============================================================

-- Admin par défaut
-- INSERT INTO users (role, username, password, first_name, last_name, name, email, admin_id)
-- VALUES ('admin', 'admin', 'admin123', 'Admin', 'User', 'Admin User', 'admin@patientportal.com', 1);
--
-- -- Médecin de test
-- INSERT INTO users (role, username, password, first_name, last_name, name, email, doctor_id)
-- VALUES ('doctor', 'jack123', 'pass123', 'Jack', 'Smith', 'Jack Smith', 'jack123@gmail.com', 1);
--
-- -- Patient de test
-- INSERT INTO users (role, username, password, first_name, last_name, name, email, patient_id)
-- VALUES ('patient', 'john123', 'pass123', 'John', 'Smith', 'John Smith', 'johnsmith123@gmail.com', 1);
