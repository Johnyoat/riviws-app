package com.hipromarketing.riviws.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.NotificationObject;
import com.hipromarketing.riviws.ui.CommentDetails;
import com.hipromarketing.riviws.utils.PostObjectHelper;
import com.hipromarketing.riviws.utils.TimeUtility;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hipromarketing.riviws.constants.Constants.COMMENT;
import static com.hipromarketing.riviws.constants.Constants.LIKED;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {
    private Context context;
    private AppCompatActivity activity;
    private List<NotificationObject> notificationObjects;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PostObjectHelper postObjectHelper = PostObjectHelper.getInstance();


    public NotificationsAdapter(Context context, AppCompatActivity activity, List<NotificationObject> notificationObjects) {
        this.context = context;
        this.activity = activity;
        this.notificationObjects = notificationObjects;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotificationHolder(LayoutInflater.from(context).inflate(R.layout.notification_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i) {
        final NotificationObject obj = notificationObjects.get(i);
        if (obj != null && obj.getUser() != null) {
            Glide.with(context).load(obj.getUser().getProfilePhotoUrl()).into(notificationHolder.profile);
            notificationHolder.message.setText(Html.fromHtml("<b>" + obj.getUser().getUserName() + "</b>  " + obj.getAction() + " your review on <br><b>" + obj.getCompany() + "</b> " + TimeUtility.getTimeAgo(Long.valueOf(obj.getDate()), System.currentTimeMillis())));
            if (obj.getAction().equalsIgnoreCase(COMMENT)) {
                Glide.with(context).load(R.drawable.comment).into(notificationHolder.action);
            } else if (obj.getAction().equalsIgnoreCase(LIKED)) {
                Glide.with(context).load(R.drawable.heart).into(notificationHolder.action);
            }
        }

        if (obj.isRead()) {
            notificationHolder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        }

        notificationHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationObject nobj = obj;
                if (obj.getAction().equalsIgnoreCase(LIKED)) {
                    nobj.setId(obj.getPostId() + "1");
                }
                postObjectHelper.readPost(nobj);
                UICreator.getInstance(activity).createDialog(CommentDetails.newInstance(nobj.getPostId()), "fromComment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationObjects.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        AppCompatTextView message;
        AppCompatImageView action;

        NotificationHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.userProfile);
            message = itemView.findViewById(R.id.message);
            action = itemView.findViewById(R.id.action);

        }
    }
}
