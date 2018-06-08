package com.miniapp.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.miniapp.R;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.Constants;

import org.androidannotations.annotations.EApplication;
import org.json.JSONException;
import org.json.JSONObject;


@EApplication
public class MiniApp extends MultiDexApplication {

    AppCompatActivity activity;
    private static MiniApp sharedApplication;
    CallbackManager callbackManager;

    public FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    ProgressDialog progressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FacebookSdk.setLegacyTokenUpgradeSupported(true);
        callbackManager = CallbackManager.Factory.create();
        sharedApplication = this;
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        CommonUtility.setDeviceID(deviceID, this);
        CommonUtility.setDeviceType(this);
        Log.d("Deviceid : ", deviceID);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MiniApp getSharedApplication() {
        return sharedApplication;
    }

    public AppCompatActivity getActivity() {
        return this.activity;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
        this.fragmentManager = this.getActivity().getSupportFragmentManager();
        CommonUtility.setStatusBarColor(activity);
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void changeScreen(Fragment fragment, Constants.FragmentAction action, int containerId) {

        if (this.fragmentManager == null) {
            if (this.getActivity() != null) {
                this.fragmentManager = this.getActivity().getSupportFragmentManager();
            }
//            else
//                Log.d(Constants.LOG_TAG, getActivity().getResources().getString(R.string.activity_does_not_exist));
        }
        String tag = fragment.getClass().getName();
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        switch (action) {
            case ADD:
                this.fragmentTransaction.add(containerId, fragment, tag).addToBackStack(tag);
                break;
            case REPLACE:
                this.fragmentTransaction.replace(containerId, fragment, tag).addToBackStack(tag);
                break;
            case REMOVE:
                this.fragmentTransaction.remove(fragment);
                this.fragmentManager.popBackStack();
                break;
        }
        this.fragmentTransaction.commitAllowingStateLoss();
    }

    public boolean popUpStack() {
        if (this.fragmentManager != null)
            if (this.fragmentManager.getBackStackEntryCount() > 0) {
                Fragment fragment = getCurrentFragment();
                this.fragmentManager.popBackStack();
                return true;
            } else {
                return false;
            }
        return false;
    }

    public void releaseMemory() {
        fragmentManager = null;
    }

    public Fragment getCurrentFragment() {
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() > 0 ?
                fragmentManager.getBackStackEntryCount() - 1 : 0).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    public Fragment getActiveFragment() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        return fragmentManager.findFragmentByTag(tag);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected)
            Toast.makeText(this, getActivity().getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        return isConnected;
    }

    public void showProgress(Context context, String... progressMessage) {
        if (this.progressDialog == null || !this.progressDialog.isShowing() && isConnected()) {
            if (progressMessage.length == 0)
                this.progressDialog = new ProgressDialog(context, R.style.MyTheme);
            else
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setMessage(progressMessage.length == 0 ? "" : progressMessage[0]);
            this.progressDialog.show();
        }
    }

    public void hideProgress() {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }


    public boolean emailValid(String email) {
        if (email.trim().length() < 1)
            return false;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.[a-z]+";
        if (email.matches(emailPattern) || email.matches(emailPattern2))
            return true;
        else
            return false;
    }

    public void scrollToBottom(ListView listView) {
        listView.setSelection(listView.getCount());
        listView.smoothScrollToPosition(listView.getCount());
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setSharedApplication(MiniApp sharedApplication) {
        MiniApp.sharedApplication = sharedApplication;
    }

    public final static boolean isValidEmail(CharSequence pTarget) {
        if (pTarget == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(pTarget).matches();
        }
    }

    public final static boolean areBothPasswordsSame(String pPassword, String pConfirmPassword) {
        return pPassword.equals(pConfirmPassword);
    }

    public void startNewActivity(Activity pPresentActivity, Class pNewActivityClass) {
        Intent intent = new Intent(pPresentActivity, pNewActivityClass);
        pPresentActivity.startActivity(intent);
        pPresentActivity.finish();
    }

    public String trimMessage(String json, String key) {
        String trimmedString = "Try again";
        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }

    public String getVolleyResonseError(Context context, VolleyError error) {
        hideProgress();
        NetworkResponse response = error.networkResponse;
        String json = "Try again";
        if (response != null) {
            json = new String(response.data);
            json = trimMessage(json, com.miniapp.commonUtils.Constants.ResponseStatus.Status_Message);
        }
        showToast(context, json);
        return json;
    }
}

