package com.hipromarketing.riviws.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Follow;
import com.hipromarketing.riviws.ui.CategoryDetails;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowViewHolder> {
    private AppCompatActivity activity;
    private Context context;
    private List<Follow> followings;
    private boolean enabled = true;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid;


    public FollowingAdapter(AppCompatActivity activity, Context context, List<Follow> followings, String uid) {
        this.context = context;
        this.followings = followings;
        this.uid = uid;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowViewHolder(LayoutInflater.from(context).inflate(R.layout.follow_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        final Follow followTag = followings.get(position);
        holder.productCompany.setText(followTag.getFollowTag());

        holder.notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followTag.isNotification()) {
                    Glide.with(context).load(R.drawable.bell).into((AppCompatImageView) v);
                    followTag.setNotification(false);
                    db.collection("users").document(uid).collection("following").document(followTag.getId()).update(new ObjectMapper().convertValue(followTag, Map.class));
                } else {
                    Glide.with(context).load(R.drawable.bell_ring).into((AppCompatImageView) v);
                    followTag.setNotification(true);
                    db.collection("users").document(uid).collection("following").document(followTag.getId()).update(new ObjectMapper().convertValue(followTag, Map.class));
                }
            }
        });

        holder.productCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UICreator.getInstance(activity).createDialog(CategoryDetails.newInstance(followTag.getFollowTag(),followTag.isFromCompany()),"catDetailsFromFollowing");
            }
        });

        if (followTag.isNotification()) {
            Glide.with(context).load(R.drawable.bell_ring).into(holder.notificationBtn);
        } else {
            Glide.with(context).load(R.drawable.bell).into(holder.notificationBtn);
        }


        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(uid).collection("following").document(followTag.getId()).delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return followings.size();
    }

    class FollowViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView notificationBtn;
        AppCompatTextView productCompany;
        AppCompatButton unfollow;

        private FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationBtn = itemView.findViewById(R.id.notificationBtn);
            productCompany = itemView.findViewById(R.id.productCompany);
            unfollow = itemView.findViewById(R.id.unfollow);
        }
    }
}
