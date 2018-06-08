package com.miniapp.signup.model;

/**
 * Created by mkaur on 11/15/17.
 */

public class SignupResponse {
    int status;
//    int statusCode;
    String message;

    public int getStaus() {
        return status;
    }

    public void setStaus(int staus) {
        this.status = staus;
    }

//    public int getStatusCode() {
//        return statusCode;
//    }
//
//    public void setStatusCode(int statusCode) {
//        this.statusCode = statusCode;
//    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
