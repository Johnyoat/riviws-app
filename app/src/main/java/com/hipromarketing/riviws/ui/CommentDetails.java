package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.RepliesAdapter;
import com.hipromarketing.riviws.models.Reply;
import com.hipromarketing.riviws.models.Trend;
import com.hipromarketing.riviws.models.User;
import com.hipromarketing.riviws.utils.PostObjectHelper;
import com.hipromarketing.riviws.utils.UICreator;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hipromarketing.riviws.constants.Constants.COMMENT;
import static com.hipromarketing.riviws.constants.Constants.LIKED;
import static com.hipromarketing.riviws.constants.Constants.getUser;
import static com.hipromarketing.riviws.utils.KeyBoardHandler.hideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentDetails extends DialogFragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View navigationBtn;
    private Trend trend;
    private AppCompatTextView likesCount;
    private AppCompatTextView likesMessage;
    private AppCompatImageView likeIc;
    private AppCompatEditText comment;
    private RecyclerView replyList;
    private boolean[] like = {false};
    String id;
    private AppCompatImageView imageView;
    private AppCompatTextView message;
    private AppCompatTextView user;
    private AppCompatTextView company;
    private CircleImageView userProfile;
    private ScaleRatingBar rating;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar progressBar;
    private LinearLayout body;

    public static CommentDetails newInstance(String id) {
        Bundle args = new Bundle();
        CommentDetails fragment = new CommentDetails();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }



    public CommentDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.comment_deatils, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatImageView bck = view.findViewById(R.id.back);
        imageView = view.findViewById(R.id.banner);
        message = view.findViewById(R.id.message);
        user = view.findViewById(R.id.user);
        company = view.findViewById(R.id.company);
        userProfile = view.findViewById(R.id.userProfile);
        rating = view.findViewById(R.id.rating);
        AppCompatImageButton send = view.findViewById(R.id.send);
        likesCount = view.findViewById(R.id.likesCount);
        likesMessage = view.findViewById(R.id.likesMessage);
        comment = view.findViewById(R.id.comment);
        replyList = view.findViewById(R.id.replies);
        navigationBtn = getActivity().findViewById(R.id.btnNav);
        LinearLayout profileInfo = view.findViewById(R.id.profileInfo);
        likeIc = view.findViewById(R.id.likeIc);
        progressBar = view.findViewById(R.id.progress);
        body = view.findViewById(R.id.body);

        body.setVisibility(View.GONE);


        if (getArguments() != null) {
//            trend = getArguments().getParcelable("trend");
            id = getArguments().getString("id");
        }

        getUser();


        navigationBtn.setVisibility(View.GONE);

//        if (getArguments() != null && trend != null) {
//
//            setUpTrend(trend);
//            setUpPageWithTrend(trend.getId());
//
//        } else
        if (id != null) {
            setUpPageWithTrend(id.trim());
        }


        likesMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(LikedPage.newInstance(trend.getId()), "toLikePage");
            }
        });

        likeIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCounter(trend);
            }
        });


        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
                navigationBtn.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!comment.getText().toString().isEmpty()) {

                    Reply reply = new Reply();
                    reply.setDate(String.valueOf(System.currentTimeMillis()));
                    reply.setUser(getUser());
                    reply.setComment(comment.getText().toString());

                    Map<String, Object> replies = new ObjectMapper().convertValue(reply, Map.class);

                    PostObjectHelper.getInstance().setAction(COMMENT, trend.getId(), trend.getCompany().getName(), trend.getUser().getUid());

                    db.collection("riviws").document(trend.getId()).update("replies", FieldValue.arrayUnion(replies));
                    comment.setText("");
                    hideKeyboard(getContext(), view);

                    navigationBtn.setVisibility(View.VISIBLE);
                }

            }
        });


        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryDetails details = CategoryDetails.newInstance(trend.getCompany());
                UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(details, "categoryDetails");
            }
        });


        profileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(ProfileDetails.newInstance(trend.getUser()), "acc");
            }
        });
    }

    private void setUpTrend(Trend trend) {
        progressBar.setVisibility(View.GONE);
        body.setVisibility(View.VISIBLE);

        for (User u : trend.getLikes()) {
            if (u != null && u.getUid().equalsIgnoreCase(firebaseUser.getUid())) {
                changeIcon(R.drawable.heart, likeIc);
                like[0] = true;
            }
        }

        if (getContext() != null) {
            message.setText(trend.getComment());
            user.setText(trend.getUser().getUserName());
            company.setText(trend.getCompany().getName());
            rating.setRating(Float.valueOf(trend.getRating()));
            Glide.with(getContext()).load(trend.getUser().getProfilePhotoUrl()).into(userProfile);
            if (trend.getImageUrl() != null & !trend.getImageUrl().isEmpty()) {
                Glide.with(getContext()).load(trend.getImageUrl()).into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        likesCount.setText(String.valueOf(trend.getLikes().size()));
    }

    private void setUpPageWithTrend(String id) {
        db.collection("riviws")
                .document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && isAdded()) {
                    Trend td = documentSnapshot.toObject(Trend.class);
                    if (td != null) {
                        setUpTrend(td);
                        trend = td;
                        likesCount.setText(String.valueOf(td.getLikes().size()));
                        Collections.reverse(td.getReplies());
                        updateRecycler(replyList, td.getReplies());
                        if (td.getLikes().size() > 1 || td.getLikes().size() == 0) {
                            likesMessage.setText("Likes");
                        } else {
                            likesMessage.setText("Like");
                        }
                        progressBar.setVisibility(View.GONE);
                        body.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error Loading", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        navigationBtn.setVisibility(View.VISIBLE);
        dismissAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationBtn.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateRecycler(RecyclerView replyList, List<Reply> replys) {
        replyList.setAdapter(new RepliesAdapter(replys, getActivity(), getContext()));
        replyList.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void likeCounter(final Trend reply) {
        ObjectMapper objectMapper = new ObjectMapper();
        DocumentReference ref = db.collection("riviws").document(reply.getId());
        Map<String, Object> likeUser = objectMapper.convertValue(getUser(), Map.class);

        if (like[0] == false) {

            ref.update("likes", FieldValue.arrayUnion(likeUser));
            likesCount.setText(String.valueOf(reply.getLikes().size() + 1));
            changeIcon(R.drawable.heart, likeIc);
            PostObjectHelper.getInstance().setAction(LIKED, reply.getId(), reply.getCompany().getName(), reply.getUser().getUid());
            like[0] = true;
        } else {

            ref.update("likes", FieldValue.arrayRemove(likeUser));
            likesCount.setText(String.valueOf(reply.getLikes().size()));
            changeIcon(R.drawable.heart_outline, likeIc);
            PostObjectHelper.getInstance().unLike(reply);
            like[0] = false;
        }
    }

    private void changeIcon(int drawable, AppCompatImageView imageView) {
        assert getActivity() != null;
        try {
            imageView.setImageDrawable(getActivity().getResources().getDrawable(drawable));
        } catch (Resources.NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
