package com.peerreview.service;

import com.peerreview.dto.AppDTOs.StudentResponse;
import com.peerreview.entity.User;
import com.peerreview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final UserRepository userRepository;

    @Autowired
    public StudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Admin: get all students
    public List<StudentResponse> getAllStudents() {
        List<User> allUsers = userRepository.findAll();
        List<StudentResponse> result = new ArrayList<>();
        for (User u : allUsers) {
            if (u.getRole() == User.Role.student) {
                StudentResponse res = new StudentResponse();
                res.setId(u.getId());
                res.setName(u.getName());
                res.setEmail(u.getEmail());
                result.add(res);
            }
        }
        return result;
    }

    // Admin: remove a student
    public void removeStudent(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (user.getRole() != User.Role.student) {
            throw new RuntimeException("User is not a student");
        }

        userRepository.deleteById(id);
    }
}
