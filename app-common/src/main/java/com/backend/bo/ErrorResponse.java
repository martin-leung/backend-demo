package com.backend.bo;

public class ErrorResponse {

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ErrorResponse(String errorMessage) {
        this.error = errorMessage;
    }
}
