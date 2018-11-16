package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.CategoriesAdapter;
import com.hipromarketing.riviws.models.Video;
import com.hipromarketing.riviws.utils.UICreator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static com.hipromarketing.riviws.constants.Constants.RECYCLER_LOAD_TIME;
import static com.hipromarketing.riviws.constants.Constants.SHOW_BANNER;
import static com.hipromarketing.riviws.constants.Constants.getCategories;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    private RecyclerView categoriesList;
    private ProgressBar progressBar;
    private SliderLayout slides;
    private RequestOptions requestOptions;
    private RelativeLayout banner;
    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    private UICreator uiCreator;

    public static Home newInstance() {

        Bundle args = new Bundle();

        Home fragment = new Home();
        fragment.setArguments(args);
        return fragment;
    }

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Video> videos = realm.where(Video.class).limit(3).findAll();


        requestOptions = new RequestOptions();
        slides = view.findViewById(R.id.slider);
        categoriesList = view.findViewById(R.id.categories);
        progressBar = view.findViewById(R.id.progress);
        banner = view.findViewById(R.id.banner);
        AppCompatButton suggest = view.findViewById(R.id.suggest);

        uiCreator = UICreator.getInstance((AppCompatActivity) getActivity());
        uiCreator.setFRAG(R.id.homeContainer);


        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                UICreator.getInstance((AppCompatActivity) getActivity()).createMinDialog(SuggestionForm.newInstance(),"suggest");
                uiCreator.createFragment(SuggestionForm.newInstance(), "videos");
            }
        });

        loadThumbnails(videos);


        videos.addChangeListener(new RealmChangeListener<RealmResults<Video>>() {
            @Override
            public void onChange(@NonNull final RealmResults<Video> videos) {
                loadThumbnails(videos);
            }
        });






        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoriesList.setLayoutManager(new GridLayoutManager(getContext(), 3));
                categoriesList.setAdapter(new CategoriesAdapter(getCategories(), getContext(), (AppCompatActivity) getActivity()));
                categoriesList.setHasFixedSize(true);
                categoriesList.setItemViewCacheSize(9);
                categoriesList.setDrawingCacheEnabled(true);
                progressBar.setVisibility(View.GONE);
                categoriesList.setVisibility(View.VISIBLE);
            }
        }, RECYCLER_LOAD_TIME);
    }

    private void bannerChanges() {
        boolean activate = Boolean.valueOf(remoteConfig.getString(SHOW_BANNER));

        if (activate){
            slides.setVisibility(View.VISIBLE);
            banner.setVisibility(View.GONE);
        }else {
            slides.setVisibility(View.GONE);
            banner.setVisibility(View.VISIBLE);
        }
    }

    private void loadThumbnails(final RealmResults<Video> videos) {
        for (Video vid : videos) {
            slides.addSlider(new TextSliderView(getContext()).image(vid.getThumbnail())
                    .description(vid.getVideoTitle())
                    .setRequestOption(requestOptions.centerCrop())
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            for (Video vid : videos) {
                                if (vid.getThumbnail().equalsIgnoreCase(slider.getUrl())) {
                                    callVideoPlayer(vid.getVideoId());
                                }
                            }
                        }
                    }));
        }
    }


    private void callVideoPlayer(String videoId) {
        UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(VideoDetails.newInstance(videoId), "vidDetails");
    }


    @Override
    public void onResume() {
        super.onResume();
//        remoteConfig.fetch().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                remoteConfig.activateFetched();
//                bannerChanges();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println(e.getMessage());
//            }
//        });
    }

}

