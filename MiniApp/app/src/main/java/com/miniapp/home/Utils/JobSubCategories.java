package com.miniapp.home.Utils;

import android.support.v4.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.dialogs.JobCategoriesDailog;
import com.miniapp.home.dialogs.JobSubCategoriesDailog;
import com.miniapp.home.fragments.AddJobFragment;
import com.miniapp.home.models.JobCategoriesResponse;
import com.miniapp.home.models.JobsCategory;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 16/04/18.
 */

public class JobSubCategories {
    MainActivity mainActivity;

    MiniApp miniApp;

    public JobSubCategories(MainActivity pActivity) {
        mainActivity = pActivity;
        miniApp = mainActivity.miniApp;
    }

    public void fetchSubCategories(final JobsCategory jobCategory, final Fragment fragment) {
        if (miniApp.isConnected()) {
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.GET_JOB_SUBCATEGORIES_URL_EXTRA +
                    jobCategory.getCategory_id();
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    JobCategoriesResponse jobCategoriesResponse = (JobCategoriesResponse) JsonUtils.toObject(resultResponse, JobCategoriesResponse.class);
                    String message = jobCategoriesResponse.getMessage();
                    if (jobCategoriesResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                        if (fragment instanceof AddJobFragment) {
                            ((AddJobFragment) fragment).addSubSpineerData(jobCategoriesResponse.getJobsCategoryList());
                        } else {
                            JobSubCategoriesDailog categoriesDailog = new JobSubCategoriesDailog(mainActivity);
                            if (!categoriesDailog.isShowingDialog()) {
                                categoriesDailog.show(fragment,jobCategory.getCategory_name(), jobCategoriesResponse.getJobsCategoryList());
                            }
                        }
                    } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                        message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                        miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                        miniApp.showToast(mainActivity, message);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    miniApp.hideProgress();
                    NetworkResponse response = error.networkResponse;
                    String json = "Try again";
                    if (response != null) {
                        json = new String(response.data);
                        json = miniApp.trimMessage(json, com.miniapp.commonUtils.Constants.ResponseStatus.Status_Message);
                        if (json != null && json.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
                            json = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                        }
                    }
                    miniApp.showToast(mainActivity, json);
                }
            });
            VolleySingleton.getInstance(mainActivity).addToRequestQueue(multipartRequest);

        } else {
//            setNoNetVisibility(View.VISIBLE);
        }
    }
}
