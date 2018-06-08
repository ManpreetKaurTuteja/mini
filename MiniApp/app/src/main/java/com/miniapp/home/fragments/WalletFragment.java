package com.miniapp.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.models.JobsResponse;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.models.UserDetails;
import com.miniapp.signup.commonUtils.Constants;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_wallet)
public class WalletFragment extends Fragment {

    @App
    MiniApp miniApp;
    MainActivity mainActivity;
    @ViewById(R.id.enter_money)
    EditText enter_money_textview;
    @ViewById(R.id.balance_textview)
    TextView balance_textview;

    public WalletFragment() {
        // Required empty public constructor
    }

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
//        fetchBalanceAndHistory();
    }

    void apiResponse(String balance) {
        balance_textview.setText(balance);
    }

    void fetchBalanceAndHistory() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(mainActivity);
            String url = com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL2 +
                    com.miniapp.commonUtils.Constants.ServiceConstants.GET_WALLET_URL_EXTRA;
            Map<String, String> headers = new HashMap<>();
            headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(mainActivity));
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.GET, url, headers, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    miniApp.hideProgress();
                    try {
                        JobsResponse jobsResponse = (JobsResponse) JsonUtils.toObject(resultResponse, JobsResponse.class);
                        String message = jobsResponse.getMessage();
                        if (jobsResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                            apiResponse(jobsResponse.getMessage());
                        } else if (message.equals(Constants.kMsgForTokenExpired) || message.equals(Constants.kMsgForYourTokenExpired)) {
                            message = Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(mainActivity, LoginActivity_.class);
                            miniApp.showToast(mainActivity, message);
                        }

                    } catch (Exception e) {
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
                        if (json != null && json.equals(Constants.kMsgForTokenExpired)) {
                            json = Constants.kMsgForSessionExpired;
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

    @Click({R.id.add_money, R.id.transfer_to_bank})
    void actions(View view) {
        switch (view.getId()) {
            case R.id.add_money:
                break;
            case R.id.transfer_to_bank:
                break;
        }
    }
}
