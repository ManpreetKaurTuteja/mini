package com.miniapp.home.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 16/04/18.
 */

public class JobCategoriesResponse {
    int status;
    int total_page;
    String message;
    @SerializedName("data")
    List<JobsCategory> jobsCategoryList;

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

    public List<JobsCategory> getJobsCategoryList() {
        return jobsCategoryList;
    }

    public void setJobsCategoryList(List<JobsCategory> jobsCategoryList) {
        this.jobsCategoryList = jobsCategoryList;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
}
