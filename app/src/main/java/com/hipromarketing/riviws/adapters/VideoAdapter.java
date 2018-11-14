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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Video;
import com.hipromarketing.riviws.ui.VideoDetails;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.Holder>  implements Filterable {
    private AppCompatActivity activity;
    private List<Video> videos;
    private List<Video> filteredVideos;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    public VideoAdapter(AppCompatActivity activity, List<Video> videos, Context context) {
        this.activity = activity;
        this.videos = videos;
        this.context = context;
    }

    public VideoAdapter(Activity activity, List<Video> videos, Context context) {
        this.activity = (AppCompatActivity) activity;
        this.videos = videos;
        this.context = context;
        this.filteredVideos = videos;
    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.video_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final boolean[] like = {false};

        final Video video = filteredVideos.get(i);

        Glide.with(context).load(video.getThumbnail()).into(holder.videoThumb);
        holder.videoTitle.setText(video.getVideoTitle());


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               likeCounter(like,holder,video);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance(activity).createDialog(VideoDetails.newInstance(video.getVideoId()),"videoDetail");
            }
        });
    }


    private void likeCounter(boolean[] like, @NonNull final VideoAdapter.Holder holder, final Video video) {
        ObjectMapper objectMapper = new ObjectMapper();
//        DocumentReference ref = db.collection("videos").document(video.getVideoId());
//        User user = new User(firebaseUser);
//        Map<String, Object> likeUser = objectMapper.convertValue(user, Map.class);

        if (like[0] == false) {

//            ref.update("likes", FieldValue.arrayUnion(likeUser));
            holder.likesCount.setText(String.valueOf(video.getLikes().size() + 1));
            changeIcon(R.drawable.heart, holder.likeIc);

            like[0] = true;
        } else {

//            ref.update("likes", FieldValue.arrayRemove(likeUser));
            holder.likesCount.setText(String.valueOf(video.getLikes().size() - 1));
            changeIcon(R.drawable.heart_outline, holder.likeIc);
            like[0] = false;
        }
    }

    @Override
    public int getItemCount() {
        return filteredVideos.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString().toLowerCase().trim();
                FilterResults results = new FilterResults();
                if (keyword.isEmpty()){
                    filteredVideos = videos;
                }else {
                    List<Video> vids = new ArrayList<>();
                    for (Video vid : videos){
                        if (vid.getVideoTitle().toLowerCase().contains(keyword)){
                            vids.add(vid);
                        }
                    }
                    filteredVideos = vids;
                }

                results.values = filteredVideos;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                        filteredVideos = (List<Video>) filterResults.values;
                        notifyDataSetChanged();
            }
        };
    }

    class Holder extends RecyclerView.ViewHolder{
        AppCompatTextView videoTitle;
        AppCompatTextView likesCount;
        AppCompatTextView repliesCount;
        AppCompatImageView videoThumb;
        AppCompatImageView likeIc;
        LinearLayout like;
        LinearLayout reply;


        public Holder(@NonNull View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            likesCount = itemView.findViewById(R.id.likeCount);
            repliesCount = itemView.findViewById(R.id.commentCount);
            videoThumb = itemView.findViewById(R.id.videoThumb);
            likeIc = itemView.findViewById(R.id.likeIc);
            like = itemView.findViewById(R.id.like);
            reply = itemView.findViewById(R.id.reply);
        }
    }

    private void changeIcon(int drawable, AppCompatImageView imageView) {
        imageView.setImageDrawable(activity.getResources().getDrawable(drawable));
    }
}
