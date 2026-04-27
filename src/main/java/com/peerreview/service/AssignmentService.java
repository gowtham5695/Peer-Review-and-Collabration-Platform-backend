package com.peerreview.service;

import com.peerreview.dto.AppDTOs.AssignmentRequest;
import com.peerreview.dto.AppDTOs.AssignmentResponse;
import com.peerreview.entity.Assignment;
import com.peerreview.entity.User;
import com.peerreview.repository.AssignmentRepository;
import com.peerreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository       = userRepository;
    }

    public AssignmentResponse create(AssignmentRequest req, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Assignment assignment = new Assignment();
        assignment.setTitle(req.getTitle());
        assignment.setDescription(req.getDescription()); // NEW
        assignment.setDeadline(req.getDeadline());
        assignment.setCreatedBy(admin);

        Assignment saved = assignmentRepository.save(assignment);
        return toResponse(saved);
    }

    public List<AssignmentResponse> getAll() {
        List<Assignment> assignments = assignmentRepository.findAllByOrderByDeadlineAsc();
        List<AssignmentResponse> result = new ArrayList<>();
        for (Assignment a : assignments) {
            result.add(toResponse(a));
        }
        return result;
    }

    public void delete(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found");
        }
        assignmentRepository.deleteById(id);
    }

    private AssignmentResponse toResponse(Assignment a) {
        AssignmentResponse res = new AssignmentResponse();
        res.setId(a.getId());
        res.setTitle(a.getTitle());
        res.setDescription(a.getDescription()); // NEW
        res.setDeadline(a.getDeadline());
        res.setCreatedBy(a.getCreatedBy().getName());
        return res;
    }
}
