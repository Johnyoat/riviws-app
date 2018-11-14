package com.hipromarketing.riviws.ui;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.VideoAdapter;
import com.hipromarketing.riviws.models.Video;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Videos extends Fragment {
    private VideoAdapter adapter;
    private BottomNavigationView bnv;
    private RecyclerView videoList;
    private Realm realm = Realm.getDefaultInstance();


    public static Videos newInstance() {

        Bundle args = new Bundle();

        Videos fragment = new Videos();
        fragment.setArguments(args);
        return fragment;
    }


    public Videos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.videos, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoList = view.findViewById(R.id.videoList);
        AppCompatEditText search = view.findViewById(R.id.search);
        bnv = getActivity().findViewById(R.id.btnNav);

//        updateRecycler(videoList, videos);


        RealmResults<Video> videos = realm.where(Video.class).findAll();
        updateRecycler(videoList, videos);

//        realm.addChangeListener(new RealmChangeListener<Realm>() {
//            @Override
//            public void onChange(@NonNull Realm realm) {
//                List<Video> videos = realm.where(Video.class).findAll();
//                updateRecycler(videoList,videos);
//            }
//        });



        videos.addChangeListener(new RealmChangeListener<RealmResults<Video>>() {
            @Override
            public void onChange(@NonNull RealmResults<Video> videos) {
                updateRecycler(videoList, videos);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) {
                    adapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) {
                    adapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (bnv != null) {
                            if (isOpen) {
                                bnv.setVisibility(View.GONE);
                            } else {
                                bnv.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void updateRecycler(RecyclerView videoList, List<Video> videos) {
        adapter = new VideoAdapter(getActivity(), videos, getContext());
        videoList.setAdapter(adapter);
        videoList.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
