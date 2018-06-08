package com.miniapp.home.models;

/**
 * Created by mac on 07/04/18.
 */

public class AddJobResponse {
    int status;
    String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
