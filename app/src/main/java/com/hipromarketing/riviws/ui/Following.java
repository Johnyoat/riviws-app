package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.FollowingAdapter;
import com.hipromarketing.riviws.models.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Following extends DialogFragment {
    private RecyclerView followingList;
    private FollowingAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static Following newInstance() {
        
        Bundle args = new Bundle();
        
        Following fragment = new Following();
        fragment.setArguments(args);
        return fragment;
    }
    
    public Following() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        followingList = view.findViewById(R.id.followingList);
        followingList.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert  user != null;
        db.collection("users").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null){
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        adapter = new FollowingAdapter(getContext(),user.getFollowing(),user.getUid());
                        followingList.setAdapter(adapter);
                    }
                }
            }
        });
    }
}
