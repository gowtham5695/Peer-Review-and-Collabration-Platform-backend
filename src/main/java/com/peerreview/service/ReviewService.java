package com.peerreview.service;

import com.peerreview.dto.AppDTOs.ReviewRequest;
import com.peerreview.dto.AppDTOs.ReviewResponse;
import com.peerreview.entity.Review;
import com.peerreview.entity.Submission;
import com.peerreview.entity.User;
import com.peerreview.repository.ReviewRepository;
import com.peerreview.repository.SubmissionRepository;
import com.peerreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         SubmissionRepository submissionRepository,
                         UserRepository userRepository) {
        this.reviewRepository     = reviewRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository       = userRepository;
    }

    // Student submits a peer review
    public ReviewResponse submitReview(ReviewRequest req, String reviewerEmail) {
        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Submission submission = submissionRepository.findById(req.getSubmissionId())
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        // Prevent reviewing your own submission
        if (submission.getStudent().getEmail().equals(reviewerEmail)) {
            throw new RuntimeException("You cannot review your own submission!");
        }

        // Prevent duplicate reviews
        boolean alreadyReviewed = reviewRepository
                .existsBySubmissionIdAndReviewerId(req.getSubmissionId(), reviewer.getId());
        if (alreadyReviewed) {
            throw new RuntimeException("You have already reviewed this submission!");
        }

        Review review = new Review();
        review.setSubmission(submission);
        review.setReviewer(reviewer);
        review.setComment(req.getComment());
        review.setRating(req.getRating());

        Review saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    // Student views feedback received on their own submissions
    public List<ReviewResponse> getMyFeedback(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Review> list = reviewRepository.findBySubmissionStudentId(student.getId());
        List<ReviewResponse> result = new ArrayList<>();
        for (Review r : list) {
            result.add(toResponse(r));
        }
        return result;
    }

    // Admin views all reviews
    public List<ReviewResponse> getAllReviews() {
        List<Review> list = reviewRepository.findAll();
        List<ReviewResponse> result = new ArrayList<>();
        for (Review r : list) {
            result.add(toResponse(r));
        }
        return result;
    }

    // Reviews for a specific submission
    public List<ReviewResponse> getReviewsForSubmission(Long submissionId) {
        List<Review> list = reviewRepository.findBySubmissionId(submissionId);
        List<ReviewResponse> result = new ArrayList<>();
        for (Review r : list) {
            result.add(toResponse(r));
        }
        return result;
    }

    private ReviewResponse toResponse(Review r) {
        ReviewResponse res = new ReviewResponse();
        res.setId(r.getId());
        res.setSubmissionId(r.getSubmission().getId());             // NEW
        res.setSubmissionFileName(r.getSubmission().getFileName()); // NEW
        res.setReviewerName(r.getReviewer().getName());
        res.setComment(r.getComment());
        res.setRating(r.getRating());
        res.setReviewedAt(r.getReviewedAt());
        return res;
    }
}
