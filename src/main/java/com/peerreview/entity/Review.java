package com.peerreview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @PrePersist
    public void prePersist() {
        this.reviewedAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Review() {}

    public Review(Submission submission, User reviewer, String comment, Integer rating) {
        this.submission = submission;
        this.reviewer   = reviewer;
        this.comment    = comment;
        this.rating     = rating;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public Submission getSubmission() { return submission; }
    public User getReviewer() { return reviewer; }
    public String getComment() { return comment; }
    public Integer getRating() { return rating; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setId(Long id) { this.id = id; }
    public void setSubmission(Submission submission) { this.submission = submission; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }
    public void setComment(String comment) { this.comment = comment; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
