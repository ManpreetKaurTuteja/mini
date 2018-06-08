package com.miniapp.login.facebook_login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac
 */

public class FbLoginResult {
    @SerializedName("id")
    String facebookId;
    @SerializedName("name")
    String userName;
//    @SerializedName("picture")
//    String profile;


    String email;
//    String gcm_id;
    String gender;
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getProfile() {
//        return profile;
//    }
//
//    public void setProfile(String profile) {
//        this.profile = profile;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
