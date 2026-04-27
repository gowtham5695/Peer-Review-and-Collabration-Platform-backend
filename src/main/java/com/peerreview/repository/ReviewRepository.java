package com.peerreview.repository;

import com.peerreview.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findBySubmissionId(Long submissionId);

    List<Review> findBySubmissionStudentId(Long studentId);

    // NEW: check if a reviewer already reviewed a submission
    boolean existsBySubmissionIdAndReviewerId(Long submissionId, Long reviewerId);
}
