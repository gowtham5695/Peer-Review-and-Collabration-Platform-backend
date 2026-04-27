package com.peerreview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public ChatMessage() {}

    public ChatMessage(Team team, User sender, String text) {
        this.team   = team;
        this.sender = sender;
        this.text   = text;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public Team getTeam() { return team; }
    public User getSender() { return sender; }
    public String getText() { return text; }
    public LocalDateTime getSentAt() { return sentAt; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setId(Long id) { this.id = id; }
    public void setTeam(Team team) { this.team = team; }
    public void setSender(User sender) { this.sender = sender; }
    public void setText(String text) { this.text = text; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
