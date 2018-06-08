package com.miniapp.home.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 06/04/18.
 */

public class JobsDetails {
    int current_page;
    int from;
    int last_page;
    int to;
    int total;
    String next_page_url;
    String path;
    String per_page;
    String prev_page_url;
    @SerializedName("data")
    List<JobData> jobDataList;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public List<JobData> getJobDataList() {
        return jobDataList;
    }

    public void setJobDataList(List<JobData> jobDataList) {
        this.jobDataList = jobDataList;
    }
}
