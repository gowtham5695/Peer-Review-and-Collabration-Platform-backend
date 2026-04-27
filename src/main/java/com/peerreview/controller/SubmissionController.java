package com.peerreview.controller;

import com.peerreview.dto.AppDTOs.SubmissionResponse;
import com.peerreview.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // POST /api/submissions/upload?assignmentId=1
    @PostMapping("/upload")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<SubmissionResponse> upload(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("file") MultipartFile file,
            Principal principal) throws IOException {
        return ResponseEntity.ok(
                submissionService.submit(assignmentId, file, principal.getName()));
    }

    // GET /api/submissions — admin sees all
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SubmissionResponse>> getAll() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    // GET /api/submissions/mine — student sees their own
    @GetMapping("/mine")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<SubmissionResponse>> getMine(Principal principal) {
        return ResponseEntity.ok(submissionService.getMySubmissions(principal.getName()));
    }

    // NEW: GET /api/submissions/others — student sees OTHER students' submissions for peer review
    @GetMapping("/others")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<SubmissionResponse>> getOthers(Principal principal) {
        return ResponseEntity.ok(submissionService.getOthersSubmissions(principal.getName()));
    }

    // NEW: GET /api/submissions/file/{filename} — download/view a submitted file
    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = "application/octet-stream";
            if (filename.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filename.toLowerCase().endsWith(".png")
                    || filename.toLowerCase().endsWith(".jpg")
                    || filename.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
