package vn.ptit.judge.dto.rest;

import vn.ptit.judge.model.ProductY;

public class ObjectResponse {
    private String requestId;
    private ProductY data;

    public ObjectResponse() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ProductY getData() {
        return data;
    }

    public void setData(ProductY data) {
        this.data = data;
    }
}