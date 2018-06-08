package com.miniapp.login.facebook_login.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mac on 14/12/17.
 */

public class FBLikes {
    @SerializedName("data")
    List<FBLikedPagesData> pagesData;
    @SerializedName("paging")
    FBPaging paging;

    public List<FBLikedPagesData> getPagesData() {
        return pagesData;
    }

    public void setPagesData(List<FBLikedPagesData> pagesData) {
        this.pagesData = pagesData;
    }

    public FBPaging getPaging() {
        return paging;
    }

    public void setPaging(FBPaging paging) {
        this.paging = paging;
    }
}
