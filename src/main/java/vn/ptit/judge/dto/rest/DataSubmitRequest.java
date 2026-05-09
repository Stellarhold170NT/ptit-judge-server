package vn.ptit.judge.dto.rest;

public class DataSubmitRequest {
    private String studentCode;
    private String qCode;
    private String requestId;
    private Integer answer;

    public DataSubmitRequest() {
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

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
}