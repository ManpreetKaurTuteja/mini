package com.miniapp.login.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.forget_password.activities.ForgotPasswordActivity;
import com.miniapp.forget_password.activities.ForgotPasswordActivity_;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.activities.MainActivity_;
import com.miniapp.login.facebook_login.MKFacebookLogin;
import com.miniapp.login.google.CMGoogleLogin;
import com.miniapp.login.model.LoginRequest;
import com.miniapp.login.model.LoginResponse;
import com.miniapp.models.UserDetails;
import com.miniapp.signup.activities.PhoneNumActivity;
import com.miniapp.signup.activities.PhoneNumActivity_;
import com.miniapp.signup.activities.SignupActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.miniapp.login.google.CMGoogleLogin.RC_SIGN_IN;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @App
    public MiniApp miniApp;

    @ViewById(R.id.signup)
    TextView signUp;
    @ViewById(R.id.email_edittext)
    EditText mEmailEditText;
    @ViewById(R.id.password_edittext)
    EditText mPasswordEditText;
    GraphRequest graphRequest;
    CallbackManager callbackManager;
    String mButtonPressedID = "";
    CMGoogleLogin googleLogin;
    public static int APP_REQUEST_CODE = 99;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
        googleLogin = new CMGoogleLogin(this);
//        CommonUtility.setStatusBarColor(this);
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    public void setLoginButtonEnabled(boolean value) {
//        loginButton.setEnabled(value);
    }

    @Click({R.id.signup, R.id.login, R.id.forget_password_text, R.id.fb_login_button, R.id.gmail_login_button})
    void clickActions(View view) {
        switch (view.getId()) {
            case R.id.signup:
//                miniApp.startNewActivity(this, PhoneNumActivity_.class);
                AccountKit.initialize(getApplicationContext());
                final Intent intent = new Intent(this, AccountKitActivity.class);
                AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                        new AccountKitConfiguration.AccountKitConfigurationBuilder(
                                LoginType.PHONE,
                                AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
                // ... perform additional configuration ...
                intent.putExtra(
                        AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                        configurationBuilder.build());
                startActivityForResult(intent, APP_REQUEST_CODE);
                break;
            case R.id.login:
                mButtonPressedID = Constants.ButtonPressedStatus.SimpleLogin;
                LoginRequest request = new LoginRequest();
                request.setEmail(mEmailEditText.getText().toString());
                request.setPassword(mPasswordEditText.getText().toString());
                login(Constants.ServiceConstants.LOGIN_URL_EXTRA, request);
                break;
            case R.id.fb_login_button:
                mButtonPressedID = Constants.ButtonPressedStatus.FBLogin;
                MKFacebookLogin facebookLogin = new MKFacebookLogin(this);
                callbackManager = miniApp.getCallbackManager();
                facebookLogin.login(callbackManager);
                break;
            case R.id.gmail_login_button:
                mButtonPressedID = Constants.ButtonPressedStatus.GoogleLogin;
                googleLogin.initialize();
                googleLogin.signIn();
                break;
            case R.id.forget_password_text:
                miniApp.startNewActivity(this, ForgotPasswordActivity_.class);
                break;
        }
    }

    public void login(String url, final LoginRequest userDetails) {
        if (miniApp.isConnected()) {
            miniApp.showProgress(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL + url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            miniApp.hideProgress();
                            LoginResponse loginResponse = (LoginResponse) JsonUtils.toObject(response, LoginResponse.class);
                            if (loginResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                                CommonUtility.setLoggedIn(true);
                                CommonUtility.setCurrentUserToken(loginResponse.getAuth_token(), LoginActivity.this);
                                CommonUtility.setCurrentUserId(loginResponse.getId(), LoginActivity.this);
                                CommonUtility.setCurrentUserDetails(loginResponse.getUserDetails(), LoginActivity.this);
                                miniApp.startNewActivity(LoginActivity.this, MainActivity_.class);
                            }
                            miniApp.showToast(LoginActivity.this, loginResponse.getMessage());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            miniApp.getVolleyResonseError(LoginActivity.this, error);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    String deviceId = "";
                    if (mButtonPressedID.equals(Constants.ButtonPressedStatus.SimpleLogin)) {
                        params.put("password", userDetails.getPassword());
                        deviceId = CommonUtility.getDeviceID(LoginActivity.this);
                    } else {
                        params.put("social_id", userDetails.getSocial_id());
                        params.put("gender", userDetails.getGender());
//                        params.put("phone_no", userDetails.getPhone_no());
                        params.put("phone_no", "46768787");
                        params.put("username", userDetails.getUsername());
                        deviceId = userDetails.getGcm_id();
                    }
                    params.put("email", userDetails.getEmail());
                    params.put("gcm_id", deviceId);
                    params.put("device_type", com.miniapp.commonUtils.Constants.DEVICE_TYPE);
                    return params;
                }

            };
            queue.add(stringRequest);
        }
    }

    private int checkPermissions() {
        String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION};
        int returnVal = 3;
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    returnVal = 1;

                } else {
                    returnVal = 2;
                    requestPermission(permissions);
                }
            }
        }
        return returnVal;
    }

    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    private boolean requestPermission(String permission, final String[] permissions) {
        final boolean[] isAllowed = new boolean[1];
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.permission));
            alertDialog.setMessage(getString(R.string.gps_permission_allow_string));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, Constants.kKeyForOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(LoginActivity.this, permissions, 1);
                    isAllowed[0] = true;
                }
            });
            alertDialog.show();
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, permissions, 1);
            isAllowed[0] = true;
        }
        return isAllowed[0];
    }

    @Override
    public void onPause() {
        super.onPause();
        miniApp.hideProgress();
    }

    protected void onStop() {
        miniApp.hideProgress();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mButtonPressedID.equals(Constants.ButtonPressedStatus.FBLogin)) {
            mButtonPressedID = "";
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (mButtonPressedID.equals(Constants.ButtonPressedStatus.GoogleLogin) && requestCode == RC_SIGN_IN) {
            mButtonPressedID = "";
            googleLogin.handleResponse(data);
        } else {

            if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
                AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
                final String[] toastMessage = new String[1];
                if (loginResult.getError() != null) {
                    toastMessage[0] = loginResult.getError().getErrorType().getMessage();
//                showErrorActivity(loginResult.getError());
                } else if (loginResult.wasCancelled()) {
                    toastMessage[0] = "Verification Cancelled";
                } else {
                    if (loginResult.getAccessToken() != null) {


                        toastMessage[0] = "Success:" + loginResult.getAccessToken().getAccountId();
                    } else {
                        toastMessage[0] = String.format(
                                "Success:%s...",
                                loginResult.getAuthorizationCode().substring(0, 10));
                    }

                    getCurrentAccount();
//                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                        @Override
//                        public void onSuccess(final Account account) {
////                            String accountKitId = account.getId();
//                            PhoneNumber phoneNumber = account.getPhoneNumber();
//                            String phoneNumberString = phoneNumber.toString();
//                            Intent intent = new Intent(LoginActivity.this, SignupActivity_.class);
//                            intent.putExtra(com.miniapp.signup.commonUtils.Constants.kKeyForPhoneNumber, phoneNumberString);
//                            LoginActivity.this.startActivity(intent);
//                            LoginActivity.this.finish();
//                        }
//
//                        @Override
//                        public void onError(final AccountKitError error) {
//                            // Handle Error
//                            toastMessage[0] = "account error";
//                        }
//                    });

                }

                // Surface the result to your user in an appropriate way.
                miniApp.showToast(this, toastMessage[0]);
            }
        }
    }


    void getCurrentAccount(){
        com.facebook.accountkit.AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            //Handle Returning User
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // Get Account Kit ID
                    String accountKitId = account.getId();
                    Log.e("Account Kit Id", accountKitId);

                    if(account.getPhoneNumber()!=null) {
//                        Log.e("CountryCode", "" + account.getPhoneNumber().getCountryCode());
//                        Log.e("PhoneNumber", "" + account.getPhoneNumber().getPhoneNumber());

                        // Get phone number
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();
                        Intent intent = new Intent(LoginActivity.this, SignupActivity_.class);
                        intent.putExtra(com.miniapp.signup.commonUtils.Constants.kKeyForPhoneNumber, phoneNumberString);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                        Log.e("NumberString", phoneNumberString);
                    }

//                    if(account.getEmail()!=null)
//                        Log.e("Email",account.getEmail());
                }

                @Override
                public void onError(final AccountKitError error) {
                    // Handle Error
                    Log.e("ERROR : ",error.toString());
                }
            });
        } else {
            //Handle new or logged out user
            Log.e("ERROR : ","Logged Out");
        }
    }
}
