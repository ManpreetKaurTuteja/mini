package com.miniapp.login.facebook_login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.MainActivity_;
import com.miniapp.login.activities.LoginActivity;
import com.miniapp.login.facebook_login.*;
import com.miniapp.login.facebook_login.commonUtils.Constants;
import com.miniapp.login.facebook_login.commonUtils.Utility;
import com.miniapp.login.facebook_login.model.FBLikedPagesData;
import com.miniapp.login.facebook_login.model.FBLikes;
import com.miniapp.login.facebook_login.model.FBPaging;
import com.miniapp.login.facebook_login.model.FbLoginResult;
import com.miniapp.login.model.LoginRequest;
import com.miniapp.login.model.LoginResponse;
import com.miniapp.models.UserDetails;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mkaur on 11/10/17.
 */

public class MKFacebookLogin {
    Context context;

    public MKFacebookLogin(Context context) {
        this.context = context;
    }

    public void login(CallbackManager callbackManager) {

        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String token = AccessToken.getCurrentAccessToken().getToken();
                                Utility.setFacebookToken(context, token);

                                LoginRequest userDetails = new LoginRequest();

                                if (object != null) {
                                    String responseString = object.toString();
                                    if (responseString != null) {
                                        FbLoginResult fbLogin = (FbLoginResult) JsonUtils.toObject(responseString, FbLoginResult.class);
                                        userDetails.setSocial_id(fbLogin.getFacebookId());
                                        userDetails.setGcm_id(fbLogin.getFacebookId());
                                        userDetails.setUsername(fbLogin.getUserName());
                                        userDetails.setDevice_type(com.miniapp.commonUtils.Constants.DEVICE_TYPE);
                                        userDetails.setEmail(fbLogin.getEmail());
                                        userDetails.setGender(fbLogin.getGender());
//                                        userDetails.setAccessToken(AccessToken.getCurrentAccessToken());
//                                        try {
//                                            URL profile_pic = new URL("https://graph.facebook.com/" + fbLogin.getFacebookId() + "/picture?width=200&height=150");
//                                            userDetails.setProfile_image(profile_pic.toString());
//                                            userDetails.setProfile_image_thumb(profile_pic.toString());
//                                        } catch (MalformedURLException e) {
//                                            e.printStackTrace();
//                                        }
//                                        fbLoginApi(userDetails);
                                        openScreen(userDetails);
                                    } else {
                                        ((LoginActivity) context).setLoginButtonEnabled(true);
                                        ((LoginActivity) context).miniApp.showToast(context, Constants.kMsgForFailedLogin);
                                    }
                                } else {
                                    ((LoginActivity) context).setLoginButtonEnabled(true);
                                    ((LoginActivity) context).miniApp.showToast(context, Constants.kMsgForFailedLogin);
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture,gender,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                ((LoginActivity) context).setLoginButtonEnabled(true);
                Log.e(Constants.TAG, error.toString());
            }
        });

    }

    void openScreen(LoginRequest userDetails) {
        ((LoginActivity) context).login(com.miniapp.commonUtils.Constants.ServiceConstants.LOGIN_FB_URL_EXTRA, userDetails);
    }

    public void fbLoginApi(final UserDetails userDetails) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, com.miniapp.commonUtils.Constants.ServiceConstants.SERVER_URL + com.miniapp.commonUtils.Constants.ServiceConstants.LOGIN_URL_EXTRA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ((LoginActivity) context).miniApp.hideProgress();
                        LoginResponse loginResponse = (LoginResponse) JsonUtils.toObject(response, LoginResponse.class);
                        if (loginResponse.getStatus() == com.miniapp.commonUtils.Constants.ResponseStatus.SUCCESS) {
                            CommonUtility.setLoggedIn(true);
                            CommonUtility.setCurrentUserDetails(loginResponse.getUserDetails(), context);
                            ((LoginActivity) context).miniApp.startNewActivity(((LoginActivity) context), MainActivity_.class);
                        }
                        ((LoginActivity) context).miniApp.showToast(context, loginResponse.getMessage());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((LoginActivity) context).miniApp.getVolleyResonseError(context, error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userDetails.getUsername());
                params.put("email", userDetails.getEmail());
                params.put("gcm_id", "dfdsf");
                params.put("device_type", com.miniapp.commonUtils.Constants.DEVICE_TYPE);
                params.put("phone_no", userDetails.getPhone_no());
                params.put("gender", userDetails.getGender());
                params.put("social_id", userDetails.getUser_id() + "");
                return params;
            }

        };
        queue.add(stringRequest);
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

}
