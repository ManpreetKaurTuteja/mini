package com.miniapp.models;

import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;
import com.miniapp.home.models.JobsCategory;
import com.miniapp.home.models.UserJobSubCategory;
import com.miniapp.home.models.UserOtherImages;
import com.miniapp.login.facebook_login.model.FBLikedPagesData;

import java.util.List;

/**
 * Created by mkaur on 10/11/17.
 * My and other users details
 */
public class UserDetails {
    int user_id;
    String profile_image;
    String profile_image_thumb;
    String email;
    String phone_no;
    String username;
    int user_type;
    int category_id;
    @SerializedName("description")
    String category_desc;
    @SerializedName("title")
    String category_name;
    String gender;
    String biography;

    @SerializedName("user_subcategory")
    List<UserJobSubCategory> userJobSubCategories;
    @SerializedName("other_images")
    List<UserOtherImages> otherImages;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getProfile_image_thumb() {
        return profile_image_thumb;
    }

    public void setProfile_image_thumb(String profile_image_thumb) {
        this.profile_image_thumb = profile_image_thumb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<UserJobSubCategory> getUserJobSubCategories() {
        return userJobSubCategories;
    }

    public void setUserJobSubCategories(List<UserJobSubCategory> userJobSubCategories) {
        this.userJobSubCategories = userJobSubCategories;
    }

    public List<UserOtherImages> getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(List<UserOtherImages> otherImages) {
        this.otherImages = otherImages;
    }
}
