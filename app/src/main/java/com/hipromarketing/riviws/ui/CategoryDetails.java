package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.TrendAdapter;
import com.hipromarketing.riviws.models.Category;
import com.hipromarketing.riviws.models.Company;
import com.hipromarketing.riviws.models.Follow;
import com.hipromarketing.riviws.models.Trend;
import com.hipromarketing.riviws.utils.UICreator;
import com.willy.ratingbar.ScaleRatingBar;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.hipromarketing.riviws.constants.Constants.getCategories;
import static com.hipromarketing.riviws.constants.Constants.getUser;
import static com.hipromarketing.riviws.utils.KeyBoardHandler.hideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryDetails extends DialogFragment {
    private TrendAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppCompatImageView searchIc;
    private SearchView searchView;
    private AppCompatImageView logo;
    private AppBarLayout appBarToolBar;
    private BottomNavigationView bnv;
    private View foobar;
    private RecyclerView reviewList;
    private String head;
    private String field;
    private AppCompatRatingBar ratingBar;
    private AppCompatTextView userViews;
    private float ratings = 0;
    private int size;
    private AppCompatButton follow;
    private boolean following = false;
    private Follow followedObj;
    private List<Follow> follows;
    private Company company;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private AppCompatImageView bck;
    private ScaleRatingBar rating;
    private AppCompatEditText comment;
    private View rateView;
    private LinearLayout addReview;
    private AppCompatImageView navigate;
    private LinearLayout ratingView;
    private AppCompatImageView banner;
    private boolean fromCompany = false;


    public static CategoryDetails newInstance(String drawable, String head) {

        Bundle args = new Bundle();

        CategoryDetails fragment = new CategoryDetails();
        args.putString("banner", drawable);
        args.putString("head", head);
        args.putString("filter", "");
        fragment.setArguments(args);
        return fragment;
    }


    public static CategoryDetails newInstance(String head, boolean fromCompany) {

        Bundle args = new Bundle();

        CategoryDetails fragment = new CategoryDetails();
        args.putString("filter", "following");
        args.putString("head", head);
        args.putBoolean("fromCompany", fromCompany);
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryDetails newInstance(Company company) {

        Bundle args = new Bundle();

        CategoryDetails fragment = new CategoryDetails();
        args.putString("head", company.getName());
        args.putString("filter", "company");
        args.putParcelable("cmp", company);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.category_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reviewList = view.findViewById(R.id.reviews);
        banner = view.findViewById(R.id.banner);
        AppCompatTextView category = view.findViewById(R.id.category);
        AppCompatImageButton send = view.findViewById(R.id.send);
        bck = view.findViewById(R.id.back);
        rating = view.findViewById(R.id.rating);
        comment = view.findViewById(R.id.comment);
        rateView = view.findViewById(R.id.rateLayout);

        getUser();

        AppCompatImageButton closeRateView = rateView.findViewById(R.id.close);

        addReview = view.findViewById(R.id.addReview);


        UICreator uiCreator = UICreator.getInstance((AppCompatActivity) getActivity());
        uiCreator.setFRAG(R.id.homeContainer);
        searchIc = view.findViewById(R.id.searchIc);
        searchView = view.findViewById(R.id.search);
        ratingView = view.findViewById(R.id.ratingInfo);
        ratingBar = view.findViewById(R.id.averageRating);
        userViews = view.findViewById(R.id.userviews);
        follow = view.findViewById(R.id.follow);


        ratingView.setVisibility(View.GONE);

        assert getActivity() != null;
        logo = getActivity().findViewById(R.id.logo);
        appBarToolBar = view.findViewById(R.id.app_bar);
        bnv = getActivity().findViewById(R.id.btnNav);
        foobar = getActivity().findViewById(R.id.foobar);
        navigate = view.findViewById(R.id.navigate);


        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_fall);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewList.setHasFixedSize(true);
        reviewList.setDrawingCacheEnabled(true);
        reviewList.setItemViewCacheSize(20);
        reviewList.setLayoutAnimation(animation);
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getArguments() != null;
                String url = company.getLocationUrl();
                if (url != null && !url.isEmpty()) {
                    UICreator.getInstance((AppCompatActivity) getActivity())
                            .createDialog(ShowLocation.newInstance(company.getLng(), company.getLat()), "showDetails");
                }
            }
        });

        int j = 0;
        int counter = 0;

        Bundle args = getArguments();
        assert args != null;
        head = args.getString("head").trim();
        StringBuilder sb = new StringBuilder(head);


        while (j < head.length()) {
            if (head.charAt(j) == ' ') {
                counter++;
                if (counter == 3) {
                    sb.setCharAt(j, '\n');
                    category.setText(sb.toString());
                    counter = 0;
                } else {
                    category.setText(head);
                }
            } else {
                category.setText(head);
            }
            j++;
        }


        final ObjectMapper objectMapper = new ObjectMapper();


        db.collection("users").document(uid).collection("following").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                follows = new ArrayList<>();
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot snap : queryDocumentSnapshots) {
                        if (snap != null) {
                            follows.add(snap.toObject(Follow.class));
                        }
                    }

                    if (follows != null) {
                        for (Follow fol : follows) {
                            if (fol.getFollowTag().trim().equalsIgnoreCase(head)) {
                                following = true;
                                if (isAdded()) {
                                    follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background));
                                    follow.setTextColor(getResources().getColor(R.color.white));
                                    follow.setText(R.string.unfollow);
                                    followedObj = fol;
                                }
                            }
                        }
                    }

                }

            }
        });

        final CollectionReference followColl = db.collection("users").document(getUser().getUid()).collection("following");
        final DocumentReference followDoc = followColl.document();

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!following) {
                    follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background));
                    follow.setTextColor(getResources().getColor(R.color.white));

                    followColl.document(followDoc.getId()).set(objectMapper.convertValue(new Follow(head, true, followDoc.getId(), fromCompany), Map.class));


                    follow.setText(getString(R.string.unfollow));
                    following = true;
                } else {
                    if (followedObj != null) {

                        followColl.document(followedObj.getId()).delete();


                        follow.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_background_shaded));
                        follow.setTextColor(getResources().getColor(R.color.colorPrimary));
                        following = false;
                        follow.setText(getString(R.string.follow));

                    }
                }

            }
        });

        foobar.setVisibility(View.GONE);

        searchIc.setVisibility(View.VISIBLE);

        searchIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setVisibility(View.VISIBLE);
                searchIc.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                bck.setVisibility(View.GONE);
                foobar.setVisibility(View.GONE);
                appBarToolBar.setExpanded(false);
                searchView.setIconified(false);
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setVisibility(View.GONE);
                searchIc.setVisibility(View.VISIBLE);
                bck.setVisibility(View.VISIBLE);
                logo.setVisibility(View.VISIBLE);
                foobar.setVisibility(View.GONE);
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
                return false;
            }
        });


        assert getActivity() != null;
        KeyboardVisibilityEvent.setEventListener(getActivity(),
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


        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.transition));


        closeRateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateView.setVisibility(View.GONE);
                reviewList.setVisibility(View.VISIBLE);
            }
        });


        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });


        if (Objects.requireNonNull(args.getString("filter")).equalsIgnoreCase("company")) {

            company = getArguments().getParcelable("cmp");
            setUpCompanyInformation();

        } else if (Objects.requireNonNull(args.getString("filter")).equalsIgnoreCase("following")) {
            if (getArguments().getBoolean("fromCompany")) {
                company = getCompany();
                setUpCompanyInformation();
            } else {
                fromCompany = false;
                head = head.toLowerCase();
                field = "company.category";
                List<Category> categories = getCategories();

                for (Category cat : categories) {
                    if (cat.getCategory().equalsIgnoreCase(head)) {
                        Glide.with(getContext()).load(cat.getImgUrl()).into(banner);
                    }
                }
            }

            loadData();

        } else {
            fromCompany = false;
            head = head.toLowerCase();
            field = "company.category";
            loadData();

            assert getContext() != null;
            addReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bnv.setSelectedItemId(R.id.post);
                    dismissAllowingStateLoss();
                }
            });
            Glide.with(getContext()).load(Integer.valueOf(args.getString("banner"))).into(banner);
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ref = db.collection("riviws").document();
                Trend trend = new Trend();

                trend.getCompany().setName(head);
                assert comment.getText() != null;
                trend.setComment(comment.getText().toString());
                trend.setDate(String.valueOf(System.currentTimeMillis()));
                trend.getCompany().setCategory(company.getCategory());
                trend.setRating(String.valueOf(rating.getRating()));
                trend.setId(ref.getId());
                trend.setUser(getUser());

                rateView.setVisibility(View.GONE);
                reviewList.setVisibility(View.VISIBLE);
                ref.set(trend);
                comment.setText("");
                rating.setRating(0);
                loadData();

                hideKeyboard(getContext(), view);

            }
        });

    }

    private void setUpCompanyInformation() {
        fromCompany = true;
        addReview.setVisibility(View.VISIBLE);
        navigate.setVisibility(View.VISIBLE);
        assert getContext() != null;
        ratingView.setVisibility(View.VISIBLE);
        if (company.getImageUrl() != null){
            Glide.with(getContext()).load(company.getImageUrl()).into(banner);
        }
        field = "company.name";
        loadData();
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateView.setVisibility(View.VISIBLE);
                reviewList.setVisibility(View.GONE);

            }
        });
    }

    private void loadData() {
        if (head != null && field != null) {
            db.collection("riviws")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .whereEqualTo(field, head).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        List<Trend> filtered = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            filtered.add(snapshot.toObject(Trend.class));
                        }

                        company = filtered.get(0).getCompany();
                        setRecyclerView(filtered);
                        ratingBar.setRating(setUpAverage(filtered));
                    }
                }
            });
        }
    }


    private Company getCompany() {
        final Company[] comp = {new Company()};
        if (head != null && field != null) {
            db.collection("riviws")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .whereEqualTo("company", head).limit(1)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots != null) {
                        List<Trend> filtered = new ArrayList<>();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            filtered.add(snapshot.toObject(Trend.class));
                        }
                        comp[0] = filtered.get(0).getCompany();
                    }
                }
            });
        }

        return comp[0];
    }

    private void setRecyclerView(List<Trend> trends) {
        size = trends.size();


        userViews.setText("According to " + trends.size() + " Users");
        adapter = new TrendAdapter(trends, getContext(), getActivity());
        reviewList.setAdapter(adapter);
    }


    private float setUpAverage(List<Trend> trends) {
        for (Trend trend : trends) {
            ratings += Float.valueOf(trend.getRating());
        }
        return ratings / size;
    }

    @Override
    public void onStop() {
        super.onStop();
        foobar.setVisibility(View.VISIBLE);
    }


}
