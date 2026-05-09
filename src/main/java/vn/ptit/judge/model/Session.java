package vn.ptit.judge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Session {
    private String token;
    private String studentCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public Session() {
    }

    @JsonCreator
    public Session(
            @JsonProperty("token") String token,
            @JsonProperty("studentCode") String studentCode,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("expiresAt") LocalDateTime expiresAt) {
        this.token = token;
        this.studentCode = studentCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}