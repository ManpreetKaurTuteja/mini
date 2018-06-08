package com.miniapp.home.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.fragments.WorkFragment_;
import com.miniapp.home.models.EditUserProfileResponse;
import com.miniapp.home.models.JobsSubCategory;
import com.miniapp.home.models.UserJobSubCategory;
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_job_detail)
public class JobDetailActivity extends AppCompatActivity {

    @App
    MiniApp miniApp;
    @ViewById(R.id.title_text)
    TextView title_text;
    @ViewById(R.id.description_text)
    TextView description_text;
    @ViewById(R.id.category_textview)
    TextView category_textview;
    @ViewById(R.id.created_at_textview)
    TextView created_at_textview;
    @ViewById(R.id.phone_text)
    TextView phone_text;
    @ViewById(R.id.email_text)
    TextView email_text;
    @ViewById(R.id.description_edittext)
    EditText msg_edittext;
    @ViewById(R.id.amount_edittext)
    EditText amount_edittext;

    @ViewById(R.id.job_image)
    ImageView jobImage;
    String jobId = "";

    @AfterViews
    void init() {
        miniApp.setActivity(this);
//        CommonUtility.setStatusBarColor(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title_text.setText(bundle.getString(Constants.kKeyForTitle));
            description_text.setText(bundle.getString(Constants.kKeyForDescription));
            created_at_textview.setText(bundle.getString(Constants.kKeyForCreatedTime));
            category_textview.setText(bundle.getString(Constants.kKeyForJobsCategories));
            if (bundle.containsKey(Constants.kKeyForJobId))
                jobId = bundle.getInt(Constants.kKeyForJobId)+"";
            if (bundle.containsKey(Constants.kKeyForPhoneNum))
                phone_text.setText(bundle.getString(Constants.kKeyForPhoneNum));
            if (bundle.containsKey(Constants.kKeyForEmail))
                email_text.setText(bundle.getString(Constants.kKeyForEmail));
            String imageUrl = bundle.getString(Constants.kKeyForImageUrl);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.NONE).
                        placeholder(R.drawable.placeholder).
                        skipMemoryCache(true).into(jobImage);
            }
        }
    }

    @Click(R.id.send_proposal)
    void sendProposal() {
            if (miniApp.isConnected()) {
                miniApp.showProgress(this);
                String url = Constants.ServiceConstants.SERVER_URL2 + Constants.ServiceConstants.SEND_PROPOSAL_URL_EXTRA;
                Map<String, String> headers = new HashMap<>();
                headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(JobDetailActivity.this));
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        miniApp.hideProgress();
                        EditUserProfileResponse editUserProfileResponse = (EditUserProfileResponse) JsonUtils.toObject(resultResponse, EditUserProfileResponse.class);
                        String message = editUserProfileResponse.getMessage();
                        if (editUserProfileResponse.getStatus() == Constants.ResponseStatus.SUCCESS) {
                            JobDetailActivity.this.finish();
                        } else if (message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForTokenExpired) || message.equals(com.miniapp.signup.commonUtils.Constants.kMsgForYourTokenExpired)) {
                            message = com.miniapp.signup.commonUtils.Constants.kMsgForSessionExpired;
                            miniApp.startNewActivity(JobDetailActivity.this, LoginActivity_.class);
                            miniApp.showToast(JobDetailActivity.this, message);
                        }
                        miniApp.showToast(JobDetailActivity.this, editUserProfileResponse.getMessage());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        miniApp.hideProgress();
                        NetworkResponse response = error.networkResponse;
                        String json = "Try again";
                        if (response != null) {
                            json = new String(response.data);
                            json = miniApp.trimMessage(json, Constants.ResponseStatus.Status_Message);
                            if (json != null && json.equals(Constants.ResponseStatus.auth_token_not_valid_msg)) {
                                json = Constants.ResponseStatus.SESSION_EXPIRED;
                            }
                        }
                        miniApp.showToast(JobDetailActivity.this, json);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(Constants.kKeyForDescription, msg_edittext.getText().toString());
                        params.put(Constants.kKeyForProposalAmount, amount_edittext.getText().toString());
                        params.put(Constants.kKeyForProposalId, jobId);

                        return params;
                    }
                };
                VolleySingleton.getInstance(JobDetailActivity.this).

                        addToRequestQueue(multipartRequest);
            } else {
//                miniApp.showToast(Constants.kMsgForUploading);
            }
//        }
//        Intent intent = new Intent(this, SendProposalActivity_.class);
//        intent.putExtra(Constants.kKeyForJobId, jobId);
//        startActivity(intent);
    }
}
