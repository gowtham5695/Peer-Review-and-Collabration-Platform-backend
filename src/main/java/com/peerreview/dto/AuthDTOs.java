package com.peerreview.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTOs {

    // ── Register Request ──────────────────────────────────────────────────────
    public static class RegisterRequest {

        @NotBlank(message = "Name is required")
        private String name;

        @Email(message = "Enter a valid email")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @NotBlank(message = "Confirm password is required")
        private String confirmPassword;

        private String role = "student";

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    // ── Login Request ─────────────────────────────────────────────────────────
    public static class LoginRequest {

        @Email(message = "Enter a valid email")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // ── Auth Response (token + user info) ─────────────────────────────────────
    public static class AuthResponse {

        private String token;
        private String name;
        private String email;
        private String role;

        public AuthResponse() {}

        public AuthResponse(String token, String name, String email, String role) {
            this.token = token;
            this.name  = name;
            this.email = email;
            this.role  = role;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
