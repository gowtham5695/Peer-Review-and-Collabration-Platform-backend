package com.peerreview.service;

import com.peerreview.dto.AppDTOs.ChatMessageRequest;
import com.peerreview.dto.AppDTOs.ChatMessageResponse;
import com.peerreview.dto.AppDTOs.TeamRequest;
import com.peerreview.dto.AppDTOs.TeamResponse;
import com.peerreview.entity.ChatMessage;
import com.peerreview.entity.Team;
import com.peerreview.entity.User;
import com.peerreview.repository.ChatMessageRepository;
import com.peerreview.repository.TeamRepository;
import com.peerreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository,
                       ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository) {
        this.teamRepository        = teamRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository        = userRepository;
    }

    public List<TeamResponse> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        List<TeamResponse> result = new ArrayList<>();
        for (Team t : teams) {
            result.add(toTeamResponse(t));
        }
        return result;
    }

    public TeamResponse createTeam(TeamRequest req) {
        Team team = new Team();
        team.setName(req.getName());
        team.setMaxMembers(req.getMaxMembers());
        Team saved = teamRepository.save(team);
        return toTeamResponse(saved);
    }

    public void deleteTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new RuntimeException("Team not found");
        }
        teamRepository.deleteById(teamId);
    }

    public TeamResponse joinTeam(Long teamId, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (team.getMembers().contains(student)) {
            return toTeamResponse(team);
        }

        int maxMembers = team.getMaxMembers() != null ? team.getMaxMembers() : 0;
        if (maxMembers > 0 && team.getMembers().size() >= maxMembers) {
            throw new RuntimeException("Team is full! Maximum " + maxMembers + " members allowed.");
        }

        team.getMembers().add(student);
        Team saved = teamRepository.save(team);
        return toTeamResponse(saved);
    }

    public TeamResponse leaveTeam(Long teamId, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.getMembers().remove(student);
        Team saved = teamRepository.save(team);
        return toTeamResponse(saved);
    }

    public List<ChatMessageResponse> getMessages(Long teamId) {
        List<ChatMessage> messages = chatMessageRepository.findByTeamIdOrderBySentAtAsc(teamId);
        List<ChatMessageResponse> result = new ArrayList<>();
        for (ChatMessage m : messages) {
            result.add(toChatResponse(m));
        }
        return result;
    }

    public ChatMessageResponse sendMessage(Long teamId,
                                            ChatMessageRequest req,
                                            String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        if (!team.getMembers().contains(sender)) {
            // ✅ FIXED: null check added here
            int maxMembers = team.getMaxMembers() != null ? team.getMaxMembers() : 0;
            if (maxMembers > 0 && team.getMembers().size() >= maxMembers) {
                throw new RuntimeException("Team is full! Maximum " + maxMembers + " members allowed.");
            }
            team.getMembers().add(sender);
            teamRepository.save(team);
        }

        ChatMessage message = new ChatMessage();
        message.setTeam(team);
        message.setSender(sender);
        message.setText(req.getText());

        ChatMessage saved = chatMessageRepository.save(message);
        return toChatResponse(saved);
    }

    private TeamResponse toTeamResponse(Team t) {
        TeamResponse res = new TeamResponse();
        res.setId(t.getId());
        res.setName(t.getName());
        res.setMemberCount(t.getMembers().size());
        res.setMaxMembers(t.getMaxMembers());
        return res;
    }

    private ChatMessageResponse toChatResponse(ChatMessage m) {
        ChatMessageResponse res = new ChatMessageResponse();
        res.setId(m.getId());
        res.setSender(m.getSender().getName());
        res.setText(m.getText());
        res.setSentAt(m.getSentAt());
        return res;
    }
}