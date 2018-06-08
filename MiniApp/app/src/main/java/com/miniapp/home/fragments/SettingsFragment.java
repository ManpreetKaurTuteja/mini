package com.miniapp.home.fragments;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.Constants;
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.activities.PrivacyPolicyActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_settings)
public class SettingsFragment extends Fragment {

    @App
    MiniApp miniApp;
    MainActivity mainActivity;

    public SettingsFragment() {
        // Required empty public constructor

    }

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
    }

    @Click({R.id.contact_us, R.id.privacy_policy, R.id.delete_account, R.id.deactivate_account})
    void actions(View view) {
        switch (view.getId()) {
            case R.id.contact_us:
                mainActivity.setTitle("Contact Us");
                miniApp.changeScreen(new ContactUsFragment_(), Constants.FragmentAction.ADD, R.id.main_activity_container);
                break;
            case R.id.privacy_policy:
                Intent intent = new Intent(mainActivity, PrivacyPolicyActivity_.class);
                mainActivity.startActivity(intent);
                break;
            case R.id.delete_account:
                break;
            case R.id.deactivate_account:
                break;
        }
    }
}
