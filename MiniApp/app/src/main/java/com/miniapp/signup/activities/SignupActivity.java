package com.miniapp.signup.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.signup.commonUtils.Constants;
import com.miniapp.signup.model.SignupResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_signup)
public class SignupActivity extends AppCompatActivity {
    @App
    MiniApp miniApp;

    @ViewById(R.id.username_edittext)
    EditText mUserNameEditText;
    @ViewById(R.id.email_edittext)
    EditText mEmailEditText;
    @ViewById(R.id.password_edittext)
    EditText mPasswordEditText;
    @ViewById(R.id.confirm_password_edittext)
    EditText mConfirmPasswordEditText;
    @ViewById(R.id.phone_edittext)
    TextView mPhoneEditText;
    @ViewById(R.id.verify_button)
    Button verifyButton;
    @ViewById(R.id.spinnerGender)
    Spinner spinnerGender;
    @ViewById(R.id.spinnerUserType)
    Spinner spinnerUserType;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
//        CommonUtility.setStatusBarColor(this);
        getPhoneNumber();
        addUserType();
        addGenderData();
    }

    void getPhoneNumber() {
        Bundle bundle = getIntent().getExtras();
        String phone = bundle.getString(Constants.kKeyForPhoneNumber);
        mPhoneEditText.setText(phone);
    }

    /*
    addGenderData will add options for gender spinner.
     */
    void addUserType() {
        List<String> list = new ArrayList<String>();
        list.add(Constants.UserType.CHOOSE);
        list.add(Constants.UserType.HIRE);
        list.add(Constants.UserType.FREELANCER);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(dataAdapter);
    }

    String getUserType() {
        String user = "";
        if (spinnerUserType.getSelectedItemPosition() != 0)
            user = spinnerUserType.getSelectedItem().toString();
        return user;
    }

    void addGenderData() {
        List<String> list = new ArrayList<String>();
        list.add(Constants.Gender.CHOOSE);
        list.add(Constants.Gender.MALE);
        list.add(Constants.Gender.FEMALE);
        list.add(Constants.Gender.OTHERS);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_item_layout, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(dataAdapter);
    }

    String getUserGender() {
        String gender = "";
        if (spinnerGender.getSelectedItemPosition() != 0)
            gender = spinnerGender.getSelectedItem().toString();
        return gender;
    }

    /*
   clickActions will handle onClick actions for login & signup button.
    */
    @Click({R.id.signup_button, R.id.login, R.id.change_button})
    void clickActions(View view) {
        switch (view.getId()) {
            case R.id.signup_button:
//                if (isNumberVerified) {
                signupUser();
//                } else {
//                    miniApp.showToast(this, Constants.kVerifyPhoneNumMsg);
//                }
                break;
            case R.id.login:
                miniApp.startNewActivity(this, LoginActivity_.class);
                break;
            case R.id.change_button:
                changePhoneNumber();
                break;
        }
    }

    public void changePhoneNumber() {
        miniApp.startNewActivity(this, PhoneNumActivity_.class);
    }

    /*
   apiResponse will do actions after successfull signup
    */
    public void apiResponse() {
        miniApp.startNewActivity(this, LoginActivity_.class);
    }

    /*
       signupUser will get email,userName,pssword and gender from respective edittexts
       and will hit api for signing the user if email is not blank and is in correct email format.
        */
    void signupUser() {
        if (miniApp.isConnected()) {
            miniApp.showProgress(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL + com.miniapp.commonUtils.Constants.ServiceConstants.REGISTER_URL_EXTRA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            miniApp.hideProgress();
                            SignupResponse registerResponse = (SignupResponse) JsonUtils.toObject(response, SignupResponse.class);
                            if (registerResponse.getStaus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                                apiResponse();
                            }else if (registerResponse.getMessage().equals(Constants.kMsgForInActiveAccount)) {
                                miniApp.startNewActivity(SignupActivity.this, LoginActivity_.class);
                            }
                            miniApp.showToast(SignupActivity.this, registerResponse.getMessage());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String error_message = miniApp.getVolleyResonseError(SignupActivity.this, error);
                            if (error_message.equals(Constants.kMsgForInActiveAccount)) {
                                miniApp.startNewActivity(SignupActivity.this, LoginActivity_.class);
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", mUserNameEditText.getText().toString());
                    params.put("password", mPasswordEditText.getText().toString());
                    params.put("confirm_password", mConfirmPasswordEditText.getText().toString());
                    params.put("email", mEmailEditText.getText().toString());
                    params.put("gender", getUserGender());
                    params.put("gcm_id", "dfdsf");
                    params.put("phone_no", mPhoneEditText.getText().toString());
                    params.put("user_type", getUserType());
                    return params;
                }

            };
            queue.add(stringRequest);
        }
    }
}
