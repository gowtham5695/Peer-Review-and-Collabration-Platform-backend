package com.peerreview.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // NEW: question/description field for admin to post queries
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    public Assignment() {}

    public Assignment(String title, String description, LocalDate deadline, User createdBy) {
        this.title       = title;
        this.description = description;
        this.deadline    = deadline;
        this.createdBy   = createdBy;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public User getCreatedBy() { return createdBy; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
