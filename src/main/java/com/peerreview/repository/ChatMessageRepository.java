package com.peerreview.repository;

import com.peerreview.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByTeamIdOrderBySentAtAsc(Long teamId);
}
