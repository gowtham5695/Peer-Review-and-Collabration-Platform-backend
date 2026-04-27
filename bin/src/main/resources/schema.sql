-- ============================================================
-- Peer Review & Collaboration Platform — MySQL Schema
-- Run this script ONCE in MySQL Workbench before starting app
-- ============================================================

CREATE DATABASE IF NOT EXISTS peer_review_db;
USE peer_review_db;

-- Users
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100)  NOT NULL,
    email    VARCHAR(150)  NOT NULL UNIQUE,
    password VARCHAR(255)  NOT NULL,
    role     ENUM('student','admin') NOT NULL DEFAULT 'student'
);

-- Assignments
CREATE TABLE IF NOT EXISTS assignments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    deadline    DATE         NOT NULL,
    created_by  BIGINT       NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

-- Submissions
CREATE TABLE IF NOT EXISTS submissions (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    assignment_id BIGINT        NOT NULL,
    student_id    BIGINT        NOT NULL,
    file_name     VARCHAR(255)  NOT NULL,
    file_path     VARCHAR(500)  NOT NULL,
    submitted_at  DATETIME      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id)    REFERENCES users(id)       ON DELETE CASCADE
);

-- Reviews
CREATE TABLE IF NOT EXISTS reviews (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    submission_id BIGINT  NOT NULL,
    reviewer_id   BIGINT  NOT NULL,
    comment       TEXT,
    rating        INT     NOT NULL CHECK (rating BETWEEN 1 AND 5),
    reviewed_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id)   REFERENCES users(id)       ON DELETE CASCADE
);

-- Teams
CREATE TABLE IF NOT EXISTS teams (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL
);

-- Team members join table
CREATE TABLE IF NOT EXISTS team_members (
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Chat messages (replaces localStorage in frontend)
CREATE TABLE IF NOT EXISTS chat_messages (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id   BIGINT  NOT NULL,
    sender_id BIGINT  NOT NULL,
    text      TEXT    NOT NULL,
    sent_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id)   REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ── Seed Data ────────────────────────────────────────────────────────────────
-- Default admin  (password = admin123  BCrypt encoded)
INSERT IGNORE INTO users (name, email, password, role)
VALUES ('Admin', 'admin@peerreview.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin');

-- Two default teams matching the frontend mockTeams
INSERT IGNORE INTO teams (id, name) VALUES (1, 'AI Research Team');
INSERT IGNORE INTO teams (id, name) VALUES (2, 'Web Dev Team');
