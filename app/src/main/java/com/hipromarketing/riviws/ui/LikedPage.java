package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.LikesAdapter;
import com.hipromarketing.riviws.models.Trend;

import androidx.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikedPage extends DialogFragment {
    RecyclerView likedList;

    public static LikedPage newInstance(String id) {

        Bundle args = new Bundle();

        LikedPage fragment = new LikedPage();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }


    public LikedPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.liked_page, container, false);
    }


    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        likedList = view.findViewById(R.id.likedList);
        AppCompatTextView title = view.findViewById(R.id.title);
        AppCompatImageView close = view.findViewById(R.id.close);
        title.setText("Likes");
        
//        List<User> users = getArguments().getParcelableArrayList("likes");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = getArguments().getString("id");

        db.collection("riviws").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null){
                    Trend trend = documentSnapshot.toObject(Trend.class);
                    if (trend != null) {
                        updateRecyler(trend);
                    }
                }
            }
        });




        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
        
        
    }

    private void updateRecyler(Trend trend) {
        LikesAdapter adapter = new LikesAdapter(trend.getLikes(),getContext(),getActivity());
        likedList.setLayoutManager(new LinearLayoutManager(getContext()));
        likedList.setAdapter(adapter);
        likedList.setHasFixedSize(true);
        likedList.setItemViewCacheSize(20);
        likedList.setDrawingCacheEnabled(true);
    }
}
