package vn.ptit.judge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Submission {
    private String id;
    private String studentCode;
    private String qCode;
    private String requestId;
    private String answer;
    private String correctAnswer;
    private String status;
    private LocalDateTime timestamp;

    public Submission() {
    }

    @JsonCreator
    public Submission(
            @JsonProperty("id") String id,
            @JsonProperty("studentCode") String studentCode,
            @JsonProperty("qCode") String qCode,
            @JsonProperty("requestId") String requestId,
            @JsonProperty("answer") String answer,
            @JsonProperty("correctAnswer") String correctAnswer,
            @JsonProperty("status") String status,
            @JsonProperty("timestamp") LocalDateTime timestamp) {
        this.id = id;
        this.studentCode = studentCode;
        this.qCode = qCode;
        this.requestId = requestId;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getQCode() {
        return qCode;
    }

    public void setQCode(String qCode) {
        this.qCode = qCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
