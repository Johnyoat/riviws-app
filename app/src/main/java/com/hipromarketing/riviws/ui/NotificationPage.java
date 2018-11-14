package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import android.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.NotificationsAdapter;
import com.hipromarketing.riviws.models.NotificationObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationPage extends DialogFragment {
    AppCompatImageView close;
    BottomNavigationView bnv;
    RecyclerView notifications;

    public static NotificationPage newInstance() {

        Bundle args = new Bundle();

        NotificationPage fragment = new NotificationPage();
        fragment.setArguments(args);
        return fragment;
    }


    public NotificationPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.notification_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close = view.findViewById(R.id.close);
        bnv = getActivity().findViewById(R.id.btnNav);
        notifications = view.findViewById(R.id.notifications);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bnv.setSelectedItemId(R.id.account);
                dismissAllowingStateLoss();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("notifications").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    List<NotificationObject> objects = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        NotificationObject obj = snapshot.toObject(NotificationObject.class);
                        if (obj != null && obj.getUser() != null) {
                            objects.add(obj);
                        }
                    }

                    updateList(objects, notifications);
                }
            }
        });


    }

    private void updateList(List<NotificationObject> objects, RecyclerView notifications) {
        notifications.setLayoutManager(new LinearLayoutManager(getContext()));
        notifications.setAdapter(new NotificationsAdapter(getContext(), (AppCompatActivity) getActivity(), objects));
    }
}
