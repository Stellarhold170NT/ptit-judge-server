package vn.ptit.judge.dto.rest;

public class CharacterResponse {
    private String requestId;
    private String data;

    public CharacterResponse() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}