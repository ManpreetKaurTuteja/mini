package com.miniapp.home.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mkaur on 10/11/17.
 * My and other users details
 */
public class ProposalDetails {
    int proposal_id;
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
    String proposal_amount;
    String proposal_msg;


    @SerializedName("user_subcategory")
    List<UserJobSubCategory> userJobSubCategories;
    @SerializedName("other_images")
    List<UserOtherImages> otherImages;

    public int getProposal_id() {
        return proposal_id;
    }

    public void setProposal_id(int proposal_id) {
        this.proposal_id = proposal_id;
    }

    public String getProposal_amount() {
        return proposal_amount;
    }

    public void setProposal_amount(String proposal_amount) {
        this.proposal_amount = proposal_amount;
    }

    public String getProposal_msg() {
        return proposal_msg;
    }

    public void setProposal_msg(String proposal_msg) {
        this.proposal_msg = proposal_msg;
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
