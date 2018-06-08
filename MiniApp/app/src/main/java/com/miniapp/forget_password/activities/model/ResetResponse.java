package com.miniapp.forget_password.activities.model;

/**
 * Created by mkaur on 11/15/17.
 */

public class ResetResponse {
    int status;
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int staus) {
        this.status = staus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
