package com.peerreview.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "max_members")
    private Integer maxMembers = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "team_members",
        joinColumns = @JoinColumn(name = "team_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }

    public Set<User> getMembers() { return members; }
    public void setMembers(Set<User> members) { this.members = members; }
}