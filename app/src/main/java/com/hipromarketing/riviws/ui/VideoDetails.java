package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.RepliesAdapter;
import com.hipromarketing.riviws.models.Reply;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.ui.PlayerUIController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.hipromarketing.riviws.constants.Constants.getUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoDetails extends DialogFragment {
    private View bnv;
    private FirebaseFirestore comments = FirebaseFirestore.getInstance();
    private DocumentReference ref;
    private RecyclerView commentList;
    private AppCompatEditText comment;

    public static VideoDetails newInstance(String videoUrl) {

        Bundle args = new Bundle();

        VideoDetails fragment = new VideoDetails();
        args.putString("videoUrl", videoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public VideoDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.video_details, container, false);
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;
        bnv = getActivity().findViewById(R.id.btnNav);
        comment = view.findViewById(R.id.message);
        AppCompatImageButton send = view.findViewById(R.id.send);
        YouTubePlayerView youtubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver((LifecycleObserver) youtubePlayerView);

        PlayerUIController uiController = youtubePlayerView.getPlayerUIController();

        uiController.showYouTubeButton(false);


        youtubePlayerView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@androidx.annotation.NonNull final YouTubePlayer initializedYouTubePlayer) {
                initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        if (getArguments() != null) {
                            initializedYouTubePlayer.loadVideo(Objects.requireNonNull(getArguments().getString("videoUrl")), 0);
                        }


                    }
                });
            }
        }, true);


        commentList = view.findViewById(R.id.comments);


        CollectionReference collectionReference = comments.collection("videos").document(getArguments().getString("videoUrl")).collection("comments");
        ref = collectionReference.document();


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<Reply> replies = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        replies.add(snapshot.toObject(Reply.class));
                    }
                    updateComments(replies);
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!comment.getText().toString().isEmpty()) {
                    Reply reply = new Reply();
                    reply.setComment(comment.getText().toString());
                 reply.setUser(getUser());
                    reply.setReplyId(ref.getId());
                    reply.setDate(String.valueOf(System.currentTimeMillis()));

                    ObjectMapper obj = new ObjectMapper();
                    Map<String, Object> comUser = obj.convertValue(reply, Map.class);
                    ref.set(comUser);
                    comment.setText("");

                }
            }
        });

    }


    private void updateComments(List<Reply> replies) {
        commentList.setAdapter(new RepliesAdapter(replies, getActivity(), getContext()));
        commentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (bnv != null) {
            bnv.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        bnv.setVisibility(View.VISIBLE);
    }
}
