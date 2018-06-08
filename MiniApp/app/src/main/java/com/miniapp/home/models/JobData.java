package com.miniapp.home.models;

/**
 * Created by mac on 06/04/18.
 */

public class JobData {
    String jobs_image_url;
    String jobs_image_thumb;

    int id;
    String title;
    String category_id;
    String description;

    String status;
    String created_at;
    String category_title;
    String phone;
    String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getJobs_image_url() {
        return jobs_image_url;
    }

    public void setJobs_image_url(String jobs_image_url) {
        this.jobs_image_url = jobs_image_url;
    }

    public String getJobs_image_thumb() {
        return jobs_image_thumb;
    }

    public void setJobs_image_thumb(String jobs_image_thumb) {
        this.jobs_image_thumb = jobs_image_thumb;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
