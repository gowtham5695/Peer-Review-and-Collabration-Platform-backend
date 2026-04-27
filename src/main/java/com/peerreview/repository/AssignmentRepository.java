package com.peerreview.repository;

import com.peerreview.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByOrderByDeadlineAsc();
}
