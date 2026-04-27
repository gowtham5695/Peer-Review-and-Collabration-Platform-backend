package com.peerreview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Submission() {}

    public Submission(Assignment assignment, User student, String fileName, String filePath) {
        this.assignment = assignment;
        this.student    = student;
        this.fileName   = fileName;
        this.filePath   = filePath;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public Assignment getAssignment() { return assignment; }
    public User getStudent() { return student; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setId(Long id) { this.id = id; }
    public void setAssignment(Assignment assignment) { this.assignment = assignment; }
    public void setStudent(User student) { this.student = student; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
