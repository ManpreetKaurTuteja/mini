package com.miniapp.signup.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
import com.miniapp.login.activities.LoginActivity_;
import com.miniapp.signup.commonUtils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_phone_num)
public class PhoneNumActivity extends AppCompatActivity {

    @App
    MiniApp miniApp;

    @ViewById(R.id.phone_edittext)
    EditText mPhoneEditText;
    @ViewById(R.id.verify_button)
    Button verifyButton;
    public static int APP_REQUEST_CODE = 99;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
//        CommonUtility.setStatusBarColor(this);
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
    }

    @Click(R.id.verify_button)
    public void phoneVerify() {
        AccountKit.initialize(getApplicationContext());
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
//                            String accountKitId = account.getId();
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();
                        toastMessage[0] = "Success:" + phoneNumberString;
                        Intent intent = new Intent(PhoneNumActivity.this, SignupActivity_.class);
                        intent.putExtra(Constants.kKeyForPhoneNumber, phoneNumberString);
                        PhoneNumActivity.this.startActivity(intent);
                        PhoneNumActivity.this.finish();
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                    }
                });
                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...

            }

            // Surface the result to your user in an appropriate way.
            miniApp.showToast(this, toastMessage[0]);
        }
    }

    @Override
    public void onBackPressed() {
        miniApp.startNewActivity(this, LoginActivity_.class);
    }
}
