package com.miniapp.home.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.miniapp.home.activities.MainActivity;
import com.miniapp.home.activities.UsersDetailActivity_;
import com.miniapp.home.fragments.WorkFragment_;
import com.miniapp.home.models.EditUserProfileResponse;
import com.miniapp.home.models.JobsSubCategory;
import com.miniapp.home.models.OtherImages;
import com.miniapp.home.models.ProposalDetails;
import com.miniapp.home.models.UserJobSubCategory;
import com.miniapp.home.models.UserOtherImages;
import com.miniapp.models.UserDetails;
import com.miniapp.volley.VolleyMultipartRequest;
import com.miniapp.volley.VolleySingleton;
import com.sasidhar.smaps.payumoney.MakePaymentActivity;
import com.sasidhar.smaps.payumoney.PayUMoney_Constants;
import com.sasidhar.smaps.payumoney.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 30/04/18.
 */

public class ProposalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View view;
    List<ProposalDetails> mProposalsList;
    Context context;
    DateFormat utc = new SimpleDateFormat("MMM dd, yyyy hh:mm aa"); //new format
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"); //received format
    int height;
    MiniApp miniApp;

    public ProposalsAdapter(List<ProposalDetails> proposalsList, Context context) {
        this.mProposalsList = proposalsList;
        this.context = context;
        miniApp = ((MainActivity) context).miniApp;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        height = display.getHeight();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposals_items_layout, parent, false);
        return new ProposalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonUtility.hideProgressDialog();
        ProposalsViewHolder jobsViewHolder = (ProposalsViewHolder) holder;
        final ProposalDetails proposalDetails = mProposalsList.get(position);
        Glide.with(context).load(proposalDetails.getProfile_image_thumb()).diskCacheStrategy(DiskCacheStrategy.NONE).
                placeholder(R.drawable.avatar).
                skipMemoryCache(true).into(jobsViewHolder.profileImage);
        final String name = proposalDetails.getUsername();
        final String amount = proposalDetails.getProposal_amount();
        final String email = proposalDetails.getEmail();
        final String phone = proposalDetails.getPhone_no();
        jobsViewHolder.nameText.setText(name);
        jobsViewHolder.contactText.setText(phone);
        jobsViewHolder.emailText.setText(email);
        final String desc = proposalDetails.getProposal_msg();
        if (desc != null && !(desc.isEmpty()))
            jobsViewHolder.descriptiontext.setText(desc);
        else
            jobsViewHolder.descriptiontext.setVisibility(View.GONE);
        jobsViewHolder.awardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                awardJob(amount, phone, email, name, desc, proposalDetails.getProposal_id() + "");
            }
        });
    }

    void awardJob(final String amount, final String phone, final String email, final String name, final String msg, final String proposalId) {
        {
            if (miniApp.isConnected()) {
                miniApp.showProgress(context);
                String url = Constants.ServiceConstants.SERVER_URL2 + Constants.ServiceConstants.AWARD_PROPOSAL_URL_EXTRA;
                Map<String, String> headers = new HashMap<>();
                headers.put(com.miniapp.commonUtils.Constants.kKeyForAuthToken, CommonUtility.getCurrentUserToken(context));
                VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        miniApp.hideProgress();
                        EditUserProfileResponse editUserProfileResponse = (EditUserProfileResponse) JsonUtils.toObject(resultResponse, EditUserProfileResponse.class);
                        if (editUserProfileResponse.getStatus() == Constants.ResponseStatus.SUCCESS) {
                            addMoney(amount,phone,email,name);
//                            ((MainActivity) context).hideTitle();
//                            miniApp.changeScreen(new WorkFragment_(), Constants.FragmentAction.REPLACE, R.id.main_activity_container);
                        }
                        miniApp.showToast(context, editUserProfileResponse.getMessage());
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
                        miniApp.showToast(context, json);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put(Constants.kKeyForPhoneNum, phone);
                        params.put(Constants.kKeyForEmail, email);
                        params.put(Constants.kKeyForUsername, name);
                        params.put(Constants.kKeyForProposalId, proposalId);
                        params.put(Constants.kKeyForDescription, msg);
                        params.put(Constants.kKeyForProposalAmount, amount);
//                        int cId = selectedCategory.getCategory_id();
//                        params.put(Constants.kKeyForCategoryId, cId + "");

//                        List<JobsSubCategory> subCategoryList = new ArrayList<>();
//                        List<UserJobSubCategory> jobsCategoryList = CommonUtility.getSelectedSubJobsCategory(context);
//                        int size = jobsCategoryList.size();
//                        for (int i = 0; i < size; i++) {
//                            params.put("user_subcategory[" + i + "][sub_category_id]", jobsCategoryList.get(i).getSubcategory_id() + "");
//                        }
                        return params;
                    }
                };
                VolleySingleton.getInstance(context).

                        addToRequestQueue(multipartRequest);
            } else {
//                miniApp.showToast(Constants.kMsgForUploading);
            }
//        }
        }
    }

    @Override
    public int getItemCount() {
        return mProposalsList.size();
    }

    void addMoney(String amount,String phone, String email,String name) {
        HashMap params = new HashMap<>();
        params.put(PayUMoney_Constants.KEY, "merchant_key>"); // Get merchant key from PayU Money Account
        params.put(PayUMoney_Constants.TXN_ID, "transaction_id");
        params.put(PayUMoney_Constants.AMOUNT, amount);
        params.put(PayUMoney_Constants.PRODUCT_INFO, "product_info");
        params.put(PayUMoney_Constants.FIRST_NAME, name);
        params.put(PayUMoney_Constants.EMAIL, email);
        params.put(PayUMoney_Constants.PHONE, phone);
        params.put(PayUMoney_Constants.SURL, "success_url");
        params.put(PayUMoney_Constants.FURL, "failure_url");


//// User defined fields are optional (pass empty string)
//        params.put(PayUMoney_Constants.UDF1, "");
//        params.put(PayUMoney_Constants.UDF2, "");
//        params.put(PayUMoney_Constants.UDF3, "");
//        params.put(PayUMoney_Constants.UDF4, "");
//        params.put(PayUMoney_Constants.UDF5, "");


// generate hash by passing params and salt
        String hash = Utils.generateHash(params,"salt"); // Get Salt from PayU Money Account
        params.put(PayUMoney_Constants.HASH, hash);


// SERVICE PROVIDER VALUE IS ALWAYS "payu_paisa".
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");

        Intent intent = new Intent(context, MakePaymentActivity.class);
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV);
        intent.putExtra(PayUMoney_Constants.PARAMS, params);
        ((Activity)context).startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
    }

    static class ProposalsViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, descriptiontext, amountText, contactText, emailText;
        CircleImageView profileImage;
        Button awardButton;

        ProposalsViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.name_textview);
            amountText = (TextView) itemView.findViewById(R.id.amount_textview);
            contactText = (TextView) itemView.findViewById(R.id.contact_textview);
            emailText = (TextView) itemView.findViewById(R.id.email_textview);
            descriptiontext = (TextView) itemView.findViewById(R.id.description_textview);
            profileImage = (CircleImageView) itemView.findViewById(R.id.dp_image);
            awardButton = (Button) itemView.findViewById(R.id.award_button);
        }
    }

    public void updateAdaptar(List<ProposalDetails> proposalsList, RecyclerView recyclerView, int lastPos) {
        this.mProposalsList = proposalsList;
        this.notifyDataSetChanged();
        recyclerView.getLayoutManager().scrollToPosition(this.mProposalsList.size() - lastPos - 1);
    }

}
