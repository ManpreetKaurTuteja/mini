package com.miniapp.forget_password.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.forget_password.activities.model.ResetResponse;
import com.miniapp.login.activities.LoginActivity;
import com.miniapp.login.activities.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_forgot_password)
public class ForgotPasswordActivity extends AppCompatActivity {

    @App
    MiniApp miniApp;
    @ViewById(R.id.email_edittext)
    EditText mEmailEditText;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
//        CommonUtility.setStatusBarColor(this);
    }

    @Click(R.id.reset_password)
    void resetPassword(View view) {
        if (miniApp.isConnected()) {
            miniApp.showProgress(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL + com.miniapp.commonUtils.Constants.ServiceConstants.FORGOT_PASSWORD_URL_EXTRA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            miniApp.hideProgress();
                            ResetResponse resetResponse = (ResetResponse) JsonUtils.toObject(response, ResetResponse.class);
                            if (resetResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                                miniApp.startNewActivity(ForgotPasswordActivity.this, LoginActivity_.class);
                            }
                            miniApp.showToast(ForgotPasswordActivity.this, resetResponse.getMessage());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            miniApp.getVolleyResonseError(ForgotPasswordActivity.this, error);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", mEmailEditText.getText().toString());
                    return params;
                }

            };
            queue.add(stringRequest);
        }
    }
}
