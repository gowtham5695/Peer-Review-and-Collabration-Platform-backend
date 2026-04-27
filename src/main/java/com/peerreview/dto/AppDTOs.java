package com.peerreview.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppDTOs {

    // ── Assignment Request ────────────────────────────────────────────────────
    public static class AssignmentRequest {

        @NotBlank(message = "Title is required")
        private String title;

        // NEW: optional description/question from admin
        private String description;

        @NotNull(message = "Deadline is required")
        private LocalDate deadline;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public LocalDate getDeadline() { return deadline; }
        public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    }

    // ── Assignment Response ───────────────────────────────────────────────────
    public static class AssignmentResponse {

        private Long id;
        private String title;
        private String description; // NEW
        private LocalDate deadline;
        private String createdBy;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public LocalDate getDeadline() { return deadline; }
        public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    }

    // ── Submission Response ───────────────────────────────────────────────────
    public static class SubmissionResponse {

        private Long id;
        private Long assignmentId;
        private String assignmentTitle;
        private String studentName;
        private String fileName;
        private String fileUrl;  // NEW: URL to download/view the file
        private LocalDateTime submittedAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getAssignmentId() { return assignmentId; }
        public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

        public String getAssignmentTitle() { return assignmentTitle; }
        public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }

        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public String getFileUrl() { return fileUrl; }
        public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

        public LocalDateTime getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    }

    // ── Review Request ────────────────────────────────────────────────────────
    public static class ReviewRequest {

        @NotNull(message = "Submission ID is required")
        private Long submissionId;

        @NotBlank(message = "Comment is required")
        private String comment;

        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating minimum is 1")
        @Max(value = 5, message = "Rating maximum is 5")
        private Integer rating;

        public Long getSubmissionId() { return submissionId; }
        public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
    }

    // ── Review Response ───────────────────────────────────────────────────────
    public static class ReviewResponse {

        private Long id;
        private Long submissionId;        // NEW
        private String submissionFileName; // NEW
        private String reviewerName;
        private String comment;
        private Integer rating;
        private LocalDateTime reviewedAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getSubmissionId() { return submissionId; }
        public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

        public String getSubmissionFileName() { return submissionFileName; }
        public void setSubmissionFileName(String submissionFileName) { this.submissionFileName = submissionFileName; }

        public String getReviewerName() { return reviewerName; }
        public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }

        public LocalDateTime getReviewedAt() { return reviewedAt; }
        public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    }

    // ── Team Request ──────────────────────────────────────────────────────────
    public static class TeamRequest {

        @NotBlank(message = "Team name is required")
        private String name;

        // NEW: max members limit (0 = unlimited)
        private Integer maxMembers = 0;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    }

    // ── Team Response ─────────────────────────────────────────────────────────
    public static class TeamResponse {

        private Long id;
        private String name;
        private Integer memberCount=0;
        private int maxMembers; // NEW

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }

        public int getMaxMembers() { return maxMembers; }
        public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }
    }

    // ── Chat Message Request ──────────────────────────────────────────────────
    public static class ChatMessageRequest {

        @NotBlank(message = "Message text is required")
        private String text;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    // ── Chat Message Response ─────────────────────────────────────────────────
    public static class ChatMessageResponse {

        private Long id;
        private String sender;
        private String text;
        private LocalDateTime sentAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public LocalDateTime getSentAt() { return sentAt; }
        public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    }

    // ── Student Response ──────────────────────────────────────────────────────
    public static class StudentResponse {

        private Long id;
        private String name;
        private String email;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // ── Join Team Request ─────────────────────────────────────────────────────
    public static class JoinTeamRequest {
        // No body needed — student joins from their JWT identity
    }
}
