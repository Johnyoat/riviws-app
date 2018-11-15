package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.hipromarketing.riviws.models.Category;
import com.hipromarketing.riviws.models.Video;
import com.hipromarketing.riviws.utils.UICreator;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static com.hipromarketing.riviws.constants.Constants.AUTO_MOBILE;
import static com.hipromarketing.riviws.constants.Constants.BARS;
import static com.hipromarketing.riviws.constants.Constants.BEAUTY_AND_SPA;
import static com.hipromarketing.riviws.constants.Constants.COSMETICS;
import static com.hipromarketing.riviws.constants.Constants.EDUCATION;
import static com.hipromarketing.riviws.constants.Constants.ENTERTAINMENT;
import static com.hipromarketing.riviws.constants.Constants.FASHION;
import static com.hipromarketing.riviws.constants.Constants.FINANCIAL_SERVICE;
import static com.hipromarketing.riviws.constants.Constants.FOOD_AND_DRINK;
import static com.hipromarketing.riviws.constants.Constants.HEALTH;
import static com.hipromarketing.riviws.constants.Constants.HEALTH_AND_MEDICAL;
import static com.hipromarketing.riviws.constants.Constants.HOME_AND_SERVICES;
import static com.hipromarketing.riviws.constants.Constants.HOTELS;
import static com.hipromarketing.riviws.constants.Constants.LOCAL_JOINT;
import static com.hipromarketing.riviws.constants.Constants.LOCAL_SERVICES;
import static com.hipromarketing.riviws.constants.Constants.MASS_MEDIA;
import static com.hipromarketing.riviws.constants.Constants.NETWORK;
import static com.hipromarketing.riviws.constants.Constants.NIGHT_LIFE;
import static com.hipromarketing.riviws.constants.Constants.PLACES;
import static com.hipromarketing.riviws.constants.Constants.PUBLIC_INST;
import static com.hipromarketing.riviws.constants.Constants.REAL_ESTATE;
import static com.hipromarketing.riviws.constants.Constants.RECYCLER_LOAD_TIME;
import static com.hipromarketing.riviws.constants.Constants.RELIGION;
import static com.hipromarketing.riviws.constants.Constants.RESTAURANTS;
import static com.hipromarketing.riviws.constants.Constants.SHOPPING;
import static com.hipromarketing.riviws.constants.Constants.SHOW_BANNER;
import static com.hipromarketing.riviws.constants.Constants.TRAVELS;
import static com.hipromarketing.riviws.constants.Constants.TRIPS;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    private RecyclerView categoriesList;
    private ProgressBar progressBar;
    private SliderLayout slides;
    private RequestOptions requestOptions;
    private RelativeLayout banner;
    long cacheExpiration = 0;
    AppCompatButton suggest;
    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    UICreator uiCreator;

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
        suggest = view.findViewById(R.id.suggest);

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


        final List<Category> categories = new ArrayList<>();

        categories.add(new Category(HOTELS, R.drawable.room));
        categories.add(new Category(RESTAURANTS, R.drawable.food));
        categories.add(new Category(FASHION, R.drawable.shoe_shirt));
        categories.add(new Category(HEALTH, R.drawable.drugs));
        categories.add(new Category(NETWORK, R.drawable.lady_phone));
        categories.add(new Category(TRIPS, R.drawable.landscape));
        categories.add(new Category(EDUCATION, R.drawable.student_boy));
        categories.add(new Category(AUTO_MOBILE, R.drawable.whitecar));
        categories.add(new Category(TRAVELS, R.drawable.bridge));
        categories.add(new Category(COSMETICS, R.drawable.cosmetics));
        categories.add(new Category(FOOD_AND_DRINK, R.drawable.food_and_drinks));
        categories.add(new Category(NIGHT_LIFE, R.drawable.night_life));
        categories.add(new Category(SHOPPING, R.drawable.shopping));
        categories.add(new Category(HEALTH_AND_MEDICAL, R.drawable.health_and_medical));
        categories.add(new Category(BEAUTY_AND_SPA, R.drawable.beauty_and_spa));
        categories.add(new Category(HOME_AND_SERVICES, R.drawable.home_and_services));
        categories.add(new Category(LOCAL_JOINT, R.drawable.local_joint));
        categories.add(new Category(LOCAL_SERVICES, R.drawable.local_business));
        categories.add(new Category(ENTERTAINMENT, R.drawable.entertainment));
        categories.add(new Category(REAL_ESTATE, R.drawable.real_estate));
        categories.add(new Category(FINANCIAL_SERVICE, R.drawable.financial_service));
        categories.add(new Category(PUBLIC_INST, R.drawable.public_institution));
        categories.add(new Category(MASS_MEDIA, R.drawable.mass_media));
        categories.add(new Category(RELIGION, R.drawable.spirituality));
        categories.add(new Category(BARS, R.drawable.bars));
        categories.add(new Category(PLACES, R.drawable.places));




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoriesList.setLayoutManager(new GridLayoutManager(getContext(), 3));
                categoriesList.setAdapter(new CategoriesAdapter(categories, getContext(), (AppCompatActivity) getActivity()));
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

