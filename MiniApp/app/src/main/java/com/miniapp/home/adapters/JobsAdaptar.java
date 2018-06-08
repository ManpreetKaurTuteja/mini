package com.miniapp.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miniapp.R;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.commonUtils.Constants;
import com.miniapp.commonUtils.DateUtility;
import com.miniapp.home.activities.JobDetailActivity;
import com.miniapp.home.activities.JobDetailActivity_;
import com.miniapp.home.models.JobData;
import com.miniapp.home.models.JobsDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by mac
 */

public class JobsAdaptar extends RecyclerView.Adapter<JobsAdaptar.JobsViewHolder> {
    View view;
    List<JobData> jobsDetailsList;
    Context context;
    DateFormat utc = new SimpleDateFormat("MMM dd, yyyy hh:mm aa"); //new format
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"); //received format
    int height;

    public JobsAdaptar(List<JobData> jobsDetailsList, Context context) {
        this.jobsDetailsList = jobsDetailsList;
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        height = display.getHeight();
    }

    @Override
    public JobsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_items_layout, parent, false);
        return new JobsAdaptar.JobsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobsViewHolder holder, final int position) {
        CommonUtility.hideProgressDialog();
        if (holder instanceof JobsViewHolder) {
            JobsViewHolder jobsViewHolder = (JobsViewHolder) holder;
            final JobData jobsDetails = jobsDetailsList.get(position);
            Glide.with(context).load(jobsDetails.getJobs_image_thumb()).diskCacheStrategy(DiskCacheStrategy.NONE).
                    placeholder(R.drawable.placeholder).
                    skipMemoryCache(true).into(jobsViewHolder.jobImage);
            final String title = jobsDetails.getTitle();
            jobsViewHolder.titleText.setText(title);
            final String category = jobsDetails.getCategory_title();
            jobsViewHolder.categorytext.setText(category);
            final String desc = jobsDetails.getDescription();
            if (desc != null && !(desc.isEmpty()))
                jobsViewHolder.descriptiontext.setText(desc);
            else
                jobsViewHolder.descriptiontext.setVisibility(View.GONE);
            String date_time = "";
            try {
                date_time = utc.format(simpleDateFormat.parse(jobsDetails.getCreated_at()));
                jobsViewHolder.createdTimeText.setText(date_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final String finalDate_time = date_time;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Job clicked", Toast.LENGTH_LONG);
                    Intent intent = new Intent(context, JobDetailActivity_.class);
                    intent.putExtra(Constants.kKeyForTitle, title);
                    intent.putExtra(Constants.kKeyForDescription, desc);
                    intent.putExtra(Constants.kKeyForJobsCategories, category);
                    intent.putExtra(Constants.kKeyForCreatedTime, DateUtility.getMsgTimeOnly(finalDate_time));
                    intent.putExtra(Constants.kKeyForPhoneNum, jobsDetailsList.get(position).getPhone());
                    intent.putExtra(Constants.kKeyForEmail, jobsDetailsList.get(position).getEmail());
                    intent.putExtra(Constants.kKeyForImageUrl, jobsDetailsList.get(position).getJobs_image_url());
                    intent.putExtra(Constants.kKeyForJobId, jobsDetailsList.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        CommonUtility.hideProgressDialog();
//        if (holder instanceof JobsViewHolder) {
//            JobsViewHolder jobsViewHolder = (JobsViewHolder) holder;
//            final JobData jobsDetails = jobsDetailsList.get(position);
//            Glide.with(context).load(jobsDetails.getJobs_image_thumb()).diskCacheStrategy(DiskCacheStrategy.NONE).
//                    placeholder(R.drawable.placeholder).
//                    skipMemoryCache(true).into(jobsViewHolder.jobImage);
//            final String title = jobsDetails.getTitle();
//            jobsViewHolder.titleText.setText(title);
//            final String category = jobsDetails.getCategory_title();
//            jobsViewHolder.categorytext.setText(category);
//            final String desc = jobsDetails.getDescription();
//            if (desc != null && !(desc.isEmpty()))
//                jobsViewHolder.descriptiontext.setText(desc);
//            else
//                jobsViewHolder.descriptiontext.setVisibility(View.GONE);
//            String date_time = "";
//            try {
//                date_time = utc.format(simpleDateFormat.parse(jobsDetails.getCreated_at()));
//                jobsViewHolder.createdTimeText.setText(date_time);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            final String finalDate_time = date_time;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "Job clicked", Toast.LENGTH_LONG);
//                    Intent intent = new Intent(context, JobDetailActivity_.class);
//                    intent.putExtra(Constants.kKeyForTitle, title);
//                    intent.putExtra(Constants.kKeyForDescription, desc);
//                    intent.putExtra(Constants.kKeyForJobsCategories, category);
//                    intent.putExtra(Constants.kKeyForCreatedTime, DateUtility.getMsgTimeOnly(finalDate_time));
//                    intent.putExtra(Constants.kKeyForPhoneNum, jobsDetailsList.get(position).getPhone());
//                    intent.putExtra(Constants.kKeyForEmail, jobsDetailsList.get(position).getEmail());
//                    intent.putExtra(Constants.kKeyForImageUrl, jobsDetailsList.get(position).getJobs_image_url());
//                    intent.putExtra(Constants.kKeyForJobId, jobsDetailsList.get(position).getId());
//                    context.startActivity(intent);
//                }
//            });
//        }
//    }

    @Override
    public int getItemCount() {
        return jobsDetailsList.size();
    }


    static class JobsViewHolder extends RecyclerView.ViewHolder {
        TextView createdTimeText, titleText, descriptiontext, categorytext;
        ImageView jobImage;

        JobsViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.title_textview);
            createdTimeText = (TextView) itemView.findViewById(R.id.created_at_textview);
            descriptiontext = (TextView) itemView.findViewById(R.id.description_textview);
            categorytext = (TextView) itemView.findViewById(R.id.category_textview);
            jobImage = (ImageView) itemView.findViewById(R.id.job_image);
        }
    }

    public void updateAdaptar(List<JobData> imageDetailses, RecyclerView recyclerView, int lastPos) {
        this.jobsDetailsList = imageDetailses;
        this.notifyDataSetChanged();
        recyclerView.getLayoutManager().scrollToPosition(this.jobsDetailsList.size() - lastPos - 1);
    }
}