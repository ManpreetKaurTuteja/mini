package com.miniapp.login.model;

import com.google.gson.annotations.SerializedName;
import com.miniapp.models.UserDetails;

/**
 * Created by mkaur on 11/15/17.
 */

public class LoginRequest {
    String email;
    String username;
    String gcm_id;
    String device_type;
    String phone_no;
    String gender;
    String social_id;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
