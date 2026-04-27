package com.peerreview.controller;

import com.peerreview.dto.AppDTOs.ReviewRequest;
import com.peerreview.dto.AppDTOs.ReviewResponse;
import com.peerreview.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // POST /api/reviews  — student submits a review (comment + rating 1-5)
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ReviewResponse> submitReview(
            @Valid @RequestBody ReviewRequest req,
            Principal principal) {
        return ResponseEntity.ok(reviewService.submitReview(req, principal.getName()));
    }

    // GET /api/reviews/my-feedback  — student views feedback on their own work
    @GetMapping("/my-feedback")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ReviewResponse>> getMyFeedback(Principal principal) {
        return ResponseEntity.ok(reviewService.getMyFeedback(principal.getName()));
    }

    // GET /api/reviews  — admin views all reviews
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // GET /api/reviews/submission/{id}  — reviews for a specific submission
    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<ReviewResponse>> getForSubmission(
            @PathVariable Long submissionId) {
        return ResponseEntity.ok(reviewService.getReviewsForSubmission(submissionId));
    }
}
