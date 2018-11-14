package com.hipromarketing.riviws.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.User;
import com.hipromarketing.riviws.ui.ProfileDetails;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikesAdapter  extends RecyclerView.Adapter<LikesAdapter.LikesViewHolder>{
    private List<User> users;
    private Context context;
    private AppCompatActivity activity;

    public LikesAdapter(List<User> users, Context context, Activity activity) {
        this.users = users;
        this.context = context;
        this.activity = (AppCompatActivity) activity;
    }

    @NonNull
    @Override
    public LikesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LikesViewHolder(LayoutInflater.from(context).inflate(R.layout.liked_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikesViewHolder likesViewHolder, int i) {
        final User user = users.get(i);
        likesViewHolder.name.setText(user.getUserName());
        Glide.with(context).load(user.getProfilePhotoUrl()).into(likesViewHolder.profile);

        likesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance(activity).createDialog(ProfileDetails.newInstance(user), "acc");
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class LikesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile;
        AppCompatTextView name;

        LikesViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
        }
    }
}
