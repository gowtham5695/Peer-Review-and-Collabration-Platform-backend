package com.peerreview.service;

import com.peerreview.dto.AppDTOs.SubmissionResponse;
import com.peerreview.entity.Assignment;
import com.peerreview.entity.Submission;
import com.peerreview.entity.User;
import com.peerreview.repository.AssignmentRepository;
import com.peerreview.repository.SubmissionRepository;
import com.peerreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
                             AssignmentRepository assignmentRepository,
                             UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository       = userRepository;
    }

    public SubmissionResponse submit(Long assignmentId,
                                     MultipartFile file,
                                     String studentEmail) throws IOException {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileName(file.getOriginalFilename());
        submission.setFilePath(filePath.toString());

        Submission saved = submissionRepository.save(submission);
        return toResponse(saved);
    }

    // Admin views all submissions
    public List<SubmissionResponse> getAllSubmissions() {
        List<Submission> list = submissionRepository.findAll();
        List<SubmissionResponse> result = new ArrayList<>();
        for (Submission s : list) {
            result.add(toResponse(s));
        }
        return result;
    }

    // Student views their own submissions
    public List<SubmissionResponse> getMySubmissions(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Submission> list = submissionRepository.findByStudentId(student.getId());
        List<SubmissionResponse> result = new ArrayList<>();
        for (Submission s : list) {
            result.add(toResponse(s));
        }
        return result;
    }

    // NEW: Student views OTHER students' submissions for peer review
    public List<SubmissionResponse> getOthersSubmissions(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get all submissions EXCEPT the current student's own
        List<Submission> list = submissionRepository.findByStudentIdNot(student.getId());
        List<SubmissionResponse> result = new ArrayList<>();
        for (Submission s : list) {
            result.add(toResponse(s));
        }
        return result;
    }

    private SubmissionResponse toResponse(Submission s) {
        SubmissionResponse res = new SubmissionResponse();
        res.setId(s.getId());
        res.setAssignmentId(s.getAssignment().getId());
        res.setAssignmentTitle(s.getAssignment().getTitle());
        res.setStudentName(s.getStudent().getName());
        res.setFileName(s.getFileName());
        // NEW: build a download URL using the stored filename
        String storedFileName = Paths.get(s.getFilePath()).getFileName().toString();
        res.setFileUrl("/api/submissions/file/" + storedFileName);
        res.setSubmittedAt(s.getSubmittedAt());
        return res;
    }
}
