package com.miniapp.home.Utils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.dialogs.JobSubCategoriesDailog;
import com.miniapp.home.fragments.AddJobFragment;
import com.miniapp.home.models.AddJobResponse;
import com.miniapp.home.models.JobCategoriesResponse;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 25/04/18.
 */

public class MLogout {
    MainActivity mainActivity;

    MiniApp miniApp;

    public MLogout(MainActivity pActivity) {
        mainActivity = pActivity;
        miniApp = mainActivity.miniApp;
    }

    public void confirmLogout() {

    }

    public void performLogout() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.LOGOUT_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    AddJobResponse logoutResponse = (AddJobResponse) JsonUtils.toObject(resultResponse, AddJobResponse.class);
                    String message = logoutResponse.getMessage();
                    if (logoutResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                        CommonUtility.setCurrentUserToken(null, mainActivity);
                        miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                    } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired)) {
//                        message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                        CommonUtility.setCurrentUserToken(null, mainActivity);
                        miniApp.startNewActivity(mainActivity, LoginActivity_.class);
//                        miniApp.showToast(mainActivity, message);
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

        }
    }
}
