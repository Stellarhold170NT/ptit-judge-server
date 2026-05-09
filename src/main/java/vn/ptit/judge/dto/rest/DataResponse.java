package vn.ptit.judge.dto.rest;

import java.util.List;

public class DataResponse {
    private String requestId;
    private List<Integer> data;

    public DataResponse() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}