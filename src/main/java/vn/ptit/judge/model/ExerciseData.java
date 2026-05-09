package vn.ptit.judge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ExerciseData {
    private String requestId;
    private String qCode;
    private Object data;
    private LocalDateTime createdAt;

    public ExerciseData() {
    }

    @JsonCreator
    public ExerciseData(
            @JsonProperty("requestId") String requestId,
            @JsonProperty("qCode") String qCode,
            @JsonProperty("data") Object data,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        this.requestId = requestId;
        this.qCode = qCode;
        this.data = data;
        this.createdAt = createdAt;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getQCode() {
        return qCode;
    }

    public void setQCode(String qCode) {
        this.qCode = qCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
