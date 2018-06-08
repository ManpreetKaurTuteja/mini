package com.miniapp.login.facebook_login.commonUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mkaur
 */

public class Utility {
    public static SharedPreferences mSharedPreferences;
    private static boolean exists = false;

    public static void initializeSharedPreference(Context pContext) {
        Utility.mSharedPreferences = pContext.getSharedPreferences(null, Context.MODE_PRIVATE);
        exists = true;
    }

    //  Access Token
    public static void setFacebookToken(Context pContext,String pToken) {
        if (mSharedPreferences == null) {
            initializeSharedPreference(pContext);
        }
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.kKeyForFacebookToken, (pToken));
        editor.commit();
    }

    public static String getFacebookToken(Context pContext) {
        if (mSharedPreferences == null) {
            initializeSharedPreference(pContext);
        }
        String facebookToken = mSharedPreferences.getString(Constants.kKeyForFacebookToken, null);
        return facebookToken;
    }
}
