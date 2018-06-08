package com.miniapp.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.miniapp.commonUtils.JsonUtils;
import com.miniapp.home.activities.JobDetailActivity_;
import com.miniapp.home.activities.UsersDetailActivity_;
import com.miniapp.home.models.JobData;
import com.miniapp.home.models.OtherImages;
import com.miniapp.home.models.UserOtherImages;
import com.miniapp.models.UserDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mac on 30/04/18.
 */

public class UsersProfilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View view;
    List<UserDetails> usersList;
    Context context;
    DateFormat utc = new SimpleDateFormat("MMM dd, yyyy hh:mm aa"); //new format
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"); //received format
    int height;

    public UsersProfilesAdapter(List<UserDetails> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        height = display.getHeight();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_profiles_items_layout, parent, false);
        return new ProfilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommonUtility.hideProgressDialog();
        ProfilesViewHolder jobsViewHolder = (ProfilesViewHolder) holder;
        final UserDetails userDetails = usersList.get(position);
        Glide.with(context).load(userDetails.getProfile_image_thumb()).diskCacheStrategy(DiskCacheStrategy.NONE).
                placeholder(R.drawable.avatar).
                skipMemoryCache(true).into(jobsViewHolder.jobImage);
        final String name = userDetails.getUsername();
        jobsViewHolder.nameText.setText(name);
        final String desc = userDetails.getBiography();
        if (desc != null && !(desc.isEmpty()))
            jobsViewHolder.descriptiontext.setText(desc);
        else
            jobsViewHolder.descriptiontext.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UsersDetailActivity_.class);
                intent.putExtra(Constants.kKeyForUsername, name);
                intent.putExtra(Constants.kKeyForDescription, desc);
                intent.putExtra(Constants.kKeyForUserType, userDetails.getUser_type());
                intent.putExtra(Constants.kKeyForEmail, userDetails.getEmail());
                intent.putExtra(Constants.kKeyForCategoryName, userDetails.getCategory_name());
                intent.putExtra(Constants.kKeyForPhoneNum, userDetails.getPhone_no());
                intent.putExtra(Constants.kKeyForUserGender, userDetails.getGender());
                intent.putExtra(Constants.kKeyForImageUrl, userDetails.getProfile_image());
                List<UserOtherImages> imagesList = userDetails.getOtherImages();
                if (imagesList != null && imagesList.size() > 0) {
                    OtherImages otherImages = new OtherImages();
                    otherImages.setUserOtherImages(imagesList);
                    String otherImagesString = JsonUtils.toString(otherImages);
                    intent.putExtra(com.miniapp.home.Utils.Constants.kKeyForUserOtherImage, otherImagesString);
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    static class ProfilesViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, descriptiontext;
        CircleImageView jobImage;

        ProfilesViewHolder(View itemView) {
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.name_textview);
            descriptiontext = (TextView) itemView.findViewById(R.id.description_textview);
            jobImage = (CircleImageView) itemView.findViewById(R.id.dp_image);
        }
    }

    public void updateAdaptar(List<UserDetails> usersList, RecyclerView recyclerView,int lastPos) {
        this.usersList = usersList;
        this.notifyDataSetChanged();
        recyclerView.getLayoutManager().scrollToPosition(this.usersList.size() - lastPos - 1);
    }

}
