package vn.ptit.judge.dto.rest;

import vn.ptit.judge.model.ProductY;

public class ObjectSubmitRequest {
    private String studentCode;
    private String qCode;
    private String requestId;
    private ProductY answer;

    public ObjectSubmitRequest() {
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

    public ProductY getAnswer() {
        return answer;
    }

    public void setAnswer(ProductY answer) {
        this.answer = answer;
    }
}