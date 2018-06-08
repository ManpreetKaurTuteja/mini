package com.miniapp.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.models.AddJobResponse;
import com.miniapp.home.models.JobsResponse;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.signup.commonUtils.Constants;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_contact_us)
public class ContactUsFragment extends Fragment {

    @App
    MiniApp miniApp;

    @ViewById(R.id.subject_edittext)
    EditText subjectEdittext;
    @ViewById(R.id.message_edittext)
    EditText messageEdittext;
    MainActivity mainActivity;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
    }

    @Click(R.id.submit)
    void submit() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL +
                    com.miniapp.commonUtils.Constants.ServiceConstants.CONTACT_US_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    AddJobResponse contactResponse = (AddJobResponse) JsonUtils.toObject(resultResponse, AddJobResponse.class);
                    String message = contactResponse.getMessage();
                    if (contactResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                        mainActivity.setTitle(Constants.kKeyForSettings);
                        miniApp.changeScreen(ContactUsFragment.this, com.miniapp.commonUtils.Constants.FragmentAction.REMOVE, R.id.main_activity_container);
                    } else if (message.equals(Constants.kMsgForTokenExpired)) {
                        message = Constants.kMsgForSessionExpired;
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
                        if (json != null && json.equals(com.miniapp.commonUtils.Constants.ResponseStatus.auth_token_not_valid_msg)) {
                            json = com.miniapp.commonUtils.Constants.ResponseStatus.SESSION_EXPIRED;
                        }
                    }
                    miniApp.showToast(mainActivity, json);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(com.miniapp.commonUtils.Constants.kKeyForSubject, subjectEdittext.getText().toString());
                    params.put(com.miniapp.commonUtils.Constants.kKeyForMessage, messageEdittext.getText().toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(mainActivity).addToRequestQueue(multipartRequest);

        }
    }
}
