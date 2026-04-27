package com.peerreview.controller;

import com.peerreview.dto.AppDTOs.StudentResponse;
import com.peerreview.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET /api/students  — admin: view all students
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // DELETE /api/students/{id}  — admin: remove a student
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok("Student removed successfully");
    }
}
