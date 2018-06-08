package com.miniapp.login.facebook_login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac on 14/12/17.
 */

public class FBPaging {
    @SerializedName("cursors")
    FBPagingCursor cursors;
    @SerializedName("previous")
    String previousPagesUrl;
    @SerializedName("next")
    String nextPagesUrl;

    public FBPagingCursor getCursors() {
        return cursors;
    }

    public void setCursors(FBPagingCursor cursors) {
        this.cursors = cursors;
    }

    public String getNextPagesUrl() {
        return nextPagesUrl;
    }

    public void setNextPagesUrl(String nextPagesUrl) {
        this.nextPagesUrl = nextPagesUrl;
    }

    public String getPreviousPagesUrl() {
        return previousPagesUrl;
    }

    public void setPreviousPagesUrl(String previousPagesUrl) {
        this.previousPagesUrl = previousPagesUrl;
    }
}
