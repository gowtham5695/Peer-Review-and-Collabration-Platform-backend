package com.peerreview.controller;

import com.peerreview.dto.AppDTOs.AssignmentRequest;
import com.peerreview.dto.AppDTOs.AssignmentResponse;
import com.peerreview.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // GET /api/assignments  — any logged-in user
    @GetMapping
    public ResponseEntity<List<AssignmentResponse>> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    // POST /api/assignments  — admin only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssignmentResponse> create(
            @Valid @RequestBody AssignmentRequest req,
            Principal principal) {
        return ResponseEntity.ok(assignmentService.create(req, principal.getName()));
    }

    // DELETE /api/assignments/{id}  — admin only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        assignmentService.delete(id);
        return ResponseEntity.ok("Assignment deleted successfully");
    }
}
