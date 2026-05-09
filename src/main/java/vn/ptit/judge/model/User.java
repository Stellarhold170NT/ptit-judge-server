package vn.ptit.judge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class User {
    private String studentCode;
    private String email;
    private String passwordHash;
    private String displayName;
    private LocalDateTime createdAt;

    public User() {
    }

    @JsonCreator
    public User(
            @JsonProperty("studentCode") String studentCode,
            @JsonProperty("email") String email,
            @JsonProperty("passwordHash") String passwordHash,
            @JsonProperty("displayName") String displayName,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.studentCode = studentCode;
        this.email = email;
        this.passwordHash = passwordHash;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
