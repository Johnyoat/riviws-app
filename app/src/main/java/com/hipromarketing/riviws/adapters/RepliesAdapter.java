package com.hipromarketing.riviws.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Reply;
import com.hipromarketing.riviws.utils.TimeUtility;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder>{

    private List<Reply> replies;
    private AppCompatActivity activity;
    private Context context;


    public RepliesAdapter(List<Reply> replies, AppCompatActivity activity, Context context) {
        this.replies = replies;
        this.activity = activity;
        this.context = context;
    }

    public RepliesAdapter(List<Reply> replies, Activity activity, Context context) {
        this.replies = replies;
        this.activity = (AppCompatActivity) activity;
        this.context = context;
    }



    @NonNull
    @Override
    public RepliesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RepliesViewHolder(LayoutInflater.from(context).inflate(R.layout.reply_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RepliesViewHolder holder, int i) {
        final boolean[] like = {false};
        final Reply reply = replies.get(i);
        holder.comment.setText(reply.getComment());
        holder.likeCount.setText(String.valueOf(reply.getLikes().size()));
        holder.username.setText(reply.getUser().getUserName());
        holder.date.setText(TimeUtility.getTimeAgo(Long.valueOf(reply.getDate()),System.currentTimeMillis()));

        Glide.with(context).load(reply.getUser().getProfilePhotoUrl()).into(holder.profile);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCounter(like, holder, reply);
            }
        });


        holder.youLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCounter(like, holder, reply);
            }
        });


    }

    private void likeCounter(boolean[] like, @NonNull RepliesViewHolder holder, Reply reply) {
        if (!like[0]){
            changeIcon(R.drawable.heart,holder.likeIc);
            holder.youLike.setText(R.string.you_liked);
            holder.youLike.setTextColor(activity.getResources().getColor(R.color.google));
            holder.likeCount.setText(String.valueOf(reply.getLikes().size() +1));
            like[0] = true;
        }else {
            changeIcon(R.drawable.heart_outline,holder.likeIc);
            holder.youLike.setText(R.string.you_like);
            holder.youLike.setTextColor(activity.getResources().getColor(R.color.black));
//                    holder.likeCount.setText(String.valueOf(Integer.valueOf(reply.getLikesCount())-1));
            holder.likeCount.setText(String.valueOf(reply.getLikes().size() -1));
            like[0] = false;
        }
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    class RepliesViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView comment,likeCount,username,date,reply,youLike;
        LinearLayout like;
        AppCompatImageView likeIc;
        CircleImageView profile;

        public RepliesViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.message);
            likeCount = itemView.findViewById(R.id.likeCount);
            username = itemView.findViewById(R.id.user);
            date = itemView.findViewById(R.id.date);
            reply = itemView.findViewById(R.id.reply);
            like = itemView.findViewById(R.id.like);
            likeIc = itemView.findViewById(R.id.likeIc);
            youLike = itemView.findViewById(R.id.youLike);
            profile = itemView.findViewById(R.id.profile);

        }
    }


    private void changeIcon(int drawable,AppCompatImageView imageView){
        imageView.setImageDrawable(activity.getResources().getDrawable(drawable));
    }

}
