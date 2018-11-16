package com.hipromarketing.riviws.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowViewHolder> {
    //    private AppCompatActivity activity;
    private Context context;
    private List<String> followings;
    private boolean enabled = true;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid;


    public FollowingAdapter(Context context, List<String> followings, String uid) {
        this.context = context;
        this.followings = followings;
        this.uid = uid;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowViewHolder(LayoutInflater.from(context).inflate(R.layout.follow_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        final String followTag = followings.get(position);
        holder.productCompany.setText(followTag);

        holder.notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enabled) {
                    Glide.with(context).load(R.drawable.bell).into((AppCompatImageView) v);
                    enabled = false;
                } else {
                    Glide.with(context).load(R.drawable.bell_ring).into((AppCompatImageView) v);
                    enabled = true;
                }
            }
        });


        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(uid).update("following", FieldValue.arrayRemove(followTag));
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

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationBtn = itemView.findViewById(R.id.notificationBtn);
            productCompany = itemView.findViewById(R.id.productCompany);
            unfollow = itemView.findViewById(R.id.unfollow);
        }
    }
}
