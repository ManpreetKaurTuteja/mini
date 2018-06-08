package com.miniapp.login.facebook_login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 14/12/17.
 */

public class FBLikedPagesData {
    @SerializedName("name")
    String pageName;
    @SerializedName("id")
    String pageId;
    @SerializedName("created_time")
    String createdTime;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
