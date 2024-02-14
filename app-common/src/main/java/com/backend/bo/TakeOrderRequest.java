package com.backend.bo;

public class TakeOrderRequest {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TakeOrderRequest{" +
                "status='" + status + '\'' +
                '}';
    }
}
