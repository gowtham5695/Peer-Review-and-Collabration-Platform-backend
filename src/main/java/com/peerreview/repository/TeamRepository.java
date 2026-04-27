package com.peerreview.repository;

import com.peerreview.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByMembersId(Long userId);
}
