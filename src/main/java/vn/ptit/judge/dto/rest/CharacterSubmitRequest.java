package vn.ptit.judge.dto.rest;

public class CharacterSubmitRequest {
    private String studentCode;
    private String qCode;
    private String requestId;
    private String answer;

    public CharacterSubmitRequest() {
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
}