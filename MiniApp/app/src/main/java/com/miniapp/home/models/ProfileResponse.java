package com.miniapp.home.models;

import com.google.gson.annotations.SerializedName;
import com.miniapp.models.UserDetails;

/**
 * Created by mac on 14/04/18.
 */

public class ProfileResponse {
    int status;
    String message;
    @SerializedName("data")
    UserDetails userDetails;

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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
