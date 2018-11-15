package com.hipromarketing.riviws.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.hipromarketing.riviws.MainActivity;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.models.Statistics;
import com.hipromarketing.riviws.models.User;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends DialogFragment {
    private Realm realm = Realm.getDefaultInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference ref;
    private ListenerRegistration listenerRegistration;
    private User fireUser;
    private AppCompatTextView reviews;
    private AppCompatTextView location;
    private AppCompatTextView occupation;
    private CircleImageView profileImage;
    private AppCompatTextView username;
    private AppCompatTextView notificationsNumber;
    private AppCompatTextView followings;

    public static Account newInstance() {

        Bundle args = new Bundle();

        Account fragment = new Account();
        fragment.setArguments(args);
        return fragment;
    }

    public Account() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.user);
        profileImage = view.findViewById(R.id.profilePicture);
        AppCompatButton logout = view.findViewById(R.id.logOut);
        AppCompatButton editProfile = view.findViewById(R.id.editProfile);
        occupation = view.findViewById(R.id.occupation);
        location = view.findViewById(R.id.location);
        followings = view.findViewById(R.id.followings);

        reviews = view.findViewById(R.id.reviews);
        notificationsNumber = view.findViewById(R.id.notificationsNumber);

        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(Trending.newInstance("user_filter"), "filtered");
            }
        });


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                getStatsFromRealm(realm);
            }
        });


        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(@NonNull Realm realm) {
                getStatsFromRealm(realm);
            }
        });

        notificationsNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(NotificationPage.newInstance(), "notification");
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.deleteAll();
                        finishUp();
                    }
                });
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        assert user != null;
        username.setText(user.getDisplayName());
        assert getContext() != null;
        Glide.with(getContext()).load(user.getPhotoUrl()).into(profileImage);

        ref = db.collection("users").document(firebaseUser.getUid());

        listenerRegistration = ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    fireUser = documentSnapshot.toObject(User.class);
                    if (fireUser != null) {
                        username.setText(fireUser.getUserName());
                        Glide.with(getContext()).load(fireUser.getProfilePhotoUrl()).into(profileImage);
                        hideViewIfTextIsEmpty(fireUser.getLocation(),location);
                        hideViewIfTextIsEmpty(fireUser.getOccupation(),occupation);
                        followings.setText(fireUser.getFollowing().size()+ " followings");
                    }
                }
            }
        });



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fireUser != null) {
                    UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(EditProfile.newInstance(fireUser), "changeProfile");
                }
            }
        });

    }

    void hideViewIfTextIsEmpty(String text,AppCompatTextView view){
        if (text.isEmpty()){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
            view.setText(text);
        }
    }

    private void finishUp() {
        FirebaseAuth.getInstance().signOut();
        assert getActivity() != null;
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    private void getStatsFromRealm(@NonNull Realm realm) {

        List<Statistics> stats = realm.where(Statistics.class).findAll();

        for (Statistics st : stats) {
            if (st.getReviews().equals("")) {
                notificationsNumber.setText(st.getNotifications()+" Notifications");
            }

            if (st.getNotifications().equals("")) {
                reviews.setText(st.getReviews()+" Reviews");
            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        listenerRegistration.remove();
    }
}
