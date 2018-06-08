package com.miniapp.home.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_send_proposal)
public class SendProposalActivity extends AppCompatActivity {
    @App
    MiniApp miniApp;
    @ViewById(R.id.msg_edittext)
    EditText msg_edittext;
    @ViewById(R.id.description_text)
    EditText bid_price_edittext;
    @ViewById(R.id.time_edittext)
    EditText time_edittext;
    String jobId;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jobId = bundle.getString(Constants.kKeyForJobId);
        }
    }

    @Click(R.id.send_proposal)
    void sendProposal() {

    }
}
