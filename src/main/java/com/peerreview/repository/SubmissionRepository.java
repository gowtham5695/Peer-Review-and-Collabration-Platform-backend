package com.peerreview.repository;

import com.peerreview.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Student's own submissions
    List<Submission> findByStudentId(Long studentId);

    // NEW: All submissions EXCEPT the current student's (for peer review)
    List<Submission> findByStudentIdNot(Long studentId);
}
