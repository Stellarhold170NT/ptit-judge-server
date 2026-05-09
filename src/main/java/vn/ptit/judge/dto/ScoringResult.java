package vn.ptit.judge.dto;

public class ScoringResult {

    private String status;
    private String message;
    private String correctAnswer;

    public ScoringResult() {
    }

    public ScoringResult(String status, String message, String correctAnswer) {
        this.status = status;
        this.message = message;
        this.correctAnswer = correctAnswer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
