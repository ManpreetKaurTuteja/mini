package com.miniapp.home.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 06/04/18.
 */

public class JobsResponse {

    int status;
    String message;
    int total_page;
    @SerializedName("data")
    List<JobData> jobDataList;

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

    public List<JobData> getJobDataList() {
        return jobDataList;
    }

    public void setJobDataList(List<JobData> jobDataList) {
        this.jobDataList = jobDataList;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
    //    public JobsDetails getJobsData() {
//        return jobsData;
//    }
//
//    public void setJobsData(JobsDetails jobsData) {
//        this.jobsData = jobsData;
//    }
}
