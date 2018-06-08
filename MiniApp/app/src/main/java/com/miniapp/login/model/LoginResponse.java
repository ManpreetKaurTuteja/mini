package com.miniapp.login.model;

import com.google.gson.annotations.SerializedName;
import com.miniapp.models.UserDetails;

/**
 * Created by mkaur on 11/15/17.
 */

public class LoginResponse {
    int status;
    String message;
    String id;
    @SerializedName("token")
    String auth_token;
    @SerializedName("user_data")
    UserDetails userDetails;

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
