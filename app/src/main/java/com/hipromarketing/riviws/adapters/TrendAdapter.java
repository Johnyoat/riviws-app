package com.hipromarketing.riviws.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.bus.Message;
import com.hipromarketing.riviws.models.Trend;
import com.hipromarketing.riviws.models.User;
import com.hipromarketing.riviws.ui.CategoryDetails;
import com.hipromarketing.riviws.ui.CommentDetails;
import com.hipromarketing.riviws.ui.ProfileDetails;
import com.hipromarketing.riviws.utils.PostObjectHelper;
import com.hipromarketing.riviws.utils.TimeUtility;
import com.hipromarketing.riviws.utils.UICreator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hipromarketing.riviws.constants.Constants.LIKED;
import static com.hipromarketing.riviws.constants.Constants.getUser;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.TrendViewHolder> implements Filterable {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private int layout = R.layout.comment_card;
    private int layout = R.layout.comapny_comment_card;
    private AppCompatActivity activity;
    private List<Trend> trends;
    private Context context;
    private List<Trend> filteredTrend;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    public TrendAdapter(List<Trend> trends, Context context, Activity activity) {
        this.activity = (AppCompatActivity) activity;
        this.trends = trends;
        this.context = context;
        this.filteredTrend = trends;
    }


    @NonNull
    @Override
    public TrendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TrendViewHolder(LayoutInflater.from(context).inflate(layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TrendViewHolder holder, int i) {
        final boolean[] like = {false};

        final Trend trend = filteredTrend.get(i);
        LayoutParams messageLayoutParams = (LayoutParams) holder.message.getLayoutParams();
        StringBuilder sb = new StringBuilder(trend.getCompany().getName());

        int j =0;
        int counter = 0;

        holder.username.setText(trend.getUser().getUserName());
        holder.message.setText(trend.getComment());

        if (trend.getImageUrl() == null | trend.getImageUrl().isEmpty()) {
            holder.banner.setVisibility(View.GONE);
            messageLayoutParams.addRule(RelativeLayout.BELOW, holder.info.getId());
            holder.message.setLayoutParams(messageLayoutParams);
            holder.username.setTextColor(activity.getResources().getColor(R.color.black));
        } else {
            setImage(trend.getImageUrl(), holder.banner);
        }


//        if (trend.getImageUrl() == null | trend.getImageUrl().isEmpty()) {
//            holder.banner.setVisibility(View.GONE);
////            messageLayoutParams.addRule(RelativeLayout.BELOW, holder.info.getId());
////            holder.message.setLayoutParams(messageLayoutParams);
////            holder.username.setTextColor(activity.getResources().getColor(R.color.black));
//        } else {
//            setImage(trend.getImageUrl(), holder.banner);
//        }

        holder.rating.setRating(Float.valueOf(trend.getRating()));

        Glide.with(context).load(trend.getUser().getProfilePhotoUrl()).into(holder.profile);
        if (holder.company != null) {

//            holder.company.setText(sb.toString());


            //This breaks the company name
            while (j < trend.getCompany().getName().length()){
                if (trend.getCompany().getName().charAt(j) == ' '){
                    counter++;
                    if (counter == 3){
                        sb.setCharAt(j,'\n');
                        holder.company.setText(sb.toString());
                        counter = 0;
                    }else{
                        holder.company.setText(sb.toString());
                    }
                }else {
                    holder.company.setText(sb.toString());
                }
                j++;
            }

        }

        holder.duration.setText(TimeUtility.getTimeAgo(Long.valueOf(trend.getDate()), System.currentTimeMillis()));

        holder.time.setText(TimeUtility.ConvertMilliSecondsTime(Long.valueOf(trend.getDate())));

        holder.likesCount.setText(String.valueOf(trend.getLikes().size()));
        holder.replyCount.setText(String.valueOf(trend.getReplies().size()));


        //For comment_card layout
//        holder.company.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CategoryDetails categoryDetails = CategoryDetails.newInstance(trend.getImageUrl(), trend.getCompany().getName(), "company", trend.getCommentType(),trend.getCompany().getLocationUrl(),trend.getCompany().getLng(),trend.getCompany().getLat());
//
//                UICreator.getInstance(activity).createDialog(categoryDetails, "categoryDetails");
//
//            }
//        });


//For company_comment_card layout
        holder.companyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryDetails categoryDetails = CategoryDetails.newInstance(trend.getCompany());
                UICreator.getInstance(activity).createDialog(categoryDetails, "categoryDetails");
            }
        });


        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance(activity).createDialog(ProfileDetails.newInstance(trend.getUser()), "acc");

            }
        });


        for (User u : trend.getLikes()) {
                if (u != null && u.getUid().equalsIgnoreCase(firebaseUser.getUid())) {
                    changeIcon(R.drawable.heart, holder.likeIc);
                    like[0] = true;
                }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callCommentDetails(holder, trend);
            }
        });


        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCommentDetails(holder, trend);
            }
        });


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCounter(like, holder, trend);
            }
        });


    }

    private void callCommentDetails(@NonNull TrendViewHolder holder, Trend trend) {
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//
//        fragmentManager.beginTransaction()
//                .addSharedElement(holder.profile, holder.profile.getTransitionName())
//                .addSharedElement(holder.banner, holder.banner.getTransitionName())
//                .replace(R.id.homeContainer, CommentDetails.newInstance(trend), "trend")
////                .addToBackStack("trend")
//                .commit();

        View bnv = activity.findViewById(R.id.btnNav);
        bnv.setVisibility(View.GONE);

        UICreator.getInstance(activity).createDialog(CommentDetails.newInstance(trend), "trend");
    }


    private void setImage(String url, AppCompatImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    @Override
    public int getItemCount() {
        return filteredTrend.size();
//        return 10;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String keyword = charSequence.toString().toLowerCase().trim();
                if (keyword.isEmpty()) {
                    filteredTrend = trends;
                } else {
                    List<Trend> filtered = new ArrayList<>();
                    for (Trend trend : trends) {
                        if (trend.getCompany().getName().toLowerCase().contains(keyword) || trend.getCompany().getName().toLowerCase().contains(keyword)) {
                            filtered.add(trend);
                        }
                    }
                    filteredTrend = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredTrend;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredTrend = (List<Trend>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    private void likeCounter(boolean[] like, @NonNull final TrendAdapter.TrendViewHolder holder, final Trend reply) {
        ObjectMapper objectMapper = new ObjectMapper();
        DocumentReference ref = db.collection("riviws").document(reply.getId());
        Map<String, Object> likeUser = objectMapper.convertValue(getUser(), Map.class);

        if (like[0] == false) {

            ref.update("likes", FieldValue.arrayUnion(likeUser));
            holder.likesCount.setText(String.valueOf(reply.getLikes().size() + 1));
            changeIcon(R.drawable.heart, holder.likeIc);
            PostObjectHelper.getInstance().setAction(LIKED, reply.getId(), reply.getCompany().getName(), reply.getUser().getUid());
            like[0] = true;
        } else {

            ref.update("likes", FieldValue.arrayRemove(likeUser));
            holder.likesCount.setText(String.valueOf(reply.getLikes().size() - 1));
            changeIcon(R.drawable.heart_outline, holder.likeIc);
            PostObjectHelper.getInstance().unLike(reply);
            like[0] = false;
        }
    }

    private void changeIcon(int drawable, AppCompatImageView imageView) {
        imageView.setImageDrawable(activity.getResources().getDrawable(drawable));
    }

    class TrendViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView username, message, likesCount, replyCount, duration, company, time;
//        AppCompatTextView username, message, likesCount, replyCount, duration, company;
        CircleImageView profile;
        AppCompatImageView banner, likeIc;
        View divider;
        RelativeLayout body, info;
        LinearLayout like;
        LinearLayout reply, companyLayout;
//        LinearLayout reply;
        AppCompatRatingBar rating;

        public TrendViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                username = itemView.findViewById(R.id.user);
                message = itemView.findViewById(R.id.message);
                likesCount = itemView.findViewById(R.id.likeCount);
                replyCount = itemView.findViewById(R.id.commentCount);
                duration = itemView.findViewById(R.id.duration);
                company = itemView.findViewById(R.id.company);
                profile = itemView.findViewById(R.id.userProfile);
                banner = itemView.findViewById(R.id.banner);
                divider = itemView.findViewById(R.id.divider);
                body = itemView.findViewById(R.id.body);
                info = itemView.findViewById(R.id.info);
                reply = itemView.findViewById(R.id.reply);
                like = itemView.findViewById(R.id.like);
                likeIc = itemView.findViewById(R.id.likeIc);
                companyLayout = itemView.findViewById(R.id.companyLayout);
                rating = itemView.findViewById(R.id.rating);
                time = itemView.findViewById(R.id.time);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
