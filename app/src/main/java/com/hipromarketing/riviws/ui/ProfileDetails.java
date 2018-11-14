package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.TrendAdapter;
import com.hipromarketing.riviws.models.Trend;
import com.hipromarketing.riviws.models.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileDetails extends DialogFragment {

    AppCompatImageView close;
    AppCompatTextView username;
    SearchView search;
    EditText editText;
    CircleImageView userProfile;
    AppCompatImageView srchBtn;
    LinearLayout detailsView;
    RecyclerView postList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TrendAdapter adapter;
    User user;
    AppCompatTextView numberOfReviews,occupation,location;


    public ProfileDetails() {
        // Required empty public constructor
    }

    public static ProfileDetails newInstance(User user) {

        Bundle args = new Bundle();

        ProfileDetails fragment = new ProfileDetails();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close = view.findViewById(R.id.close);
        username = view.findViewById(R.id.user);
        search = view.findViewById(R.id.search);
        editText = search.findViewById(androidx.appcompat.R.id.search_src_text);
        userProfile = view.findViewById(R.id.userProfile);
        srchBtn = view.findViewById(R.id.searchBtn);
        detailsView = view.findViewById(R.id.detailsLayout);
        postList = view.findViewById(R.id.postList);
        numberOfReviews = view.findViewById(R.id.numberOfReviews);
        location = view.findViewById(R.id.location);
        occupation = view.findViewById(R.id.occupation);

        int white = getActivity().getResources().getColor(R.color.white);
        editText.setTextColor(white);
        editText.setHintTextColor(white);

        srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setIconified(false);
                detailsView.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                srchBtn.setVisibility(View.GONE);

            }
        });




        if (getArguments() != null){

            user = getArguments().getParcelable("user");
            username.setText(user.getUserName());
            editText.setHint("Search " + user.getUserName() + "'s reviews ");
            occupation.setText(user.getOccupation());
            location.setText(user.getLocation());

            Glide.with(getContext()).load(user.getProfilePhotoUrl()).into(userProfile);

            db.collection("riviws").orderBy("date", Query.Direction.DESCENDING)
                    .whereEqualTo("user.uid", user.getUid())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            if (queryDocumentSnapshots != null) {
                                final List<Trend> list = new ArrayList<>();
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                    list.add(snapshot.toObject(Trend.class));
                                }

                                numberOfReviews.setText(String.valueOf(list.size()));
                                populatePostList(list);
                            }
                        }
                    });
        }


        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search.setVisibility(View.GONE);
                detailsView.setVisibility(View.VISIBLE);
                srchBtn.setVisibility(View.VISIBLE);
                return false;
            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if (adapter != null){
                    adapter.getFilter().filter(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (adapter != null){
                    adapter.getFilter().filter(s);
                }
                return false;
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    private void populatePostList(List<Trend> trends) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new TrendAdapter(trends, getContext(), getActivity());
        postList.setLayoutManager(mLayoutManager);
        postList.setAdapter(adapter);
        postList.setHasFixedSize(true);
        postList.setItemViewCacheSize(20);
        postList.setDrawingCacheEnabled(true);
        adapter.notifyItemChanged(0, adapter.getItemCount());
    }

}
