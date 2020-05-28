package com.hipromarketing.riviws.ui;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.TrendAdapter;
import com.hipromarketing.riviws.models.Trend;
import com.hipromarketing.riviws.utils.UICreator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.hipromarketing.riviws.constants.Constants.RECYCLER_LOAD_TIME;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trending extends DialogFragment {
    private RecyclerView trendingList;
    private TrendAdapter adapter;
    private Parcelable listSate;
    private ProgressBar progressBar;
    private View btnNav;
    private List<Trend> trendsCompany = new ArrayList<>();

    public static Trending newInstance(String filter) {

        Bundle args = new Bundle();

        Trending fragment = new Trending();
        args.putString("filter", filter);
        fragment.setArguments(args);
        return fragment;
    }


    public Trending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trending, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatEditText search = view.findViewById(R.id.search);

        assert getActivity() != null;
        btnNav = getActivity().findViewById(R.id.btnNav);

        trendingList = view.findViewById(R.id.trendList);
        progressBar = view.findViewById(R.id.progress);


        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_fall);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        trendingList.setLayoutManager(mLayoutManager);
//        trendingList.setHasFixedSize(true);
        trendingList.setItemViewCacheSize(20);
        trendingList.setDrawingCacheEnabled(true);
        trendingList.setLayoutAnimation(animation);
        assert trendingList.getLayoutManager() != null;
        listSate = trendingList.getLayoutManager().onSaveInstanceState();
        trendingList.getLayoutManager().onRestoreInstanceState(listSate);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (getArguments() != null) {
            String key = getArguments().getString("filter");
            Query query = db.collection("riviws").orderBy("date", Query.Direction.DESCENDING);
            if (key != null) {
                switch (key) {
                    case "default":
                        queryListener(query);
                        break;
                    case "user_filter":
                        assert user != null;
                        queryListener(query.whereEqualTo("user.uid", user.getUid()));
                        break;
                    default:
                        break;
                }
            }
        }


        final ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) trendingList.getLayoutParams();

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (btnNav != null) {
                            if (isOpen) {
                                bottomNavigationVisibilityControl(View.GONE);
                                margins.setMargins(0, 0, 0, 0);
                                trendingList.setLayoutParams(margins);
                            } else {
                                bottomNavigationVisibilityControl(View.VISIBLE);
                                margins.setMargins(0, 0, 0, 60);
                                trendingList.setLayoutParams(margins);
                            }
                        }
                    }
                });


        search.setKeyListener(null);
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                if (trendsCompany.size() > 0){
                    UICreator.getInstance((AppCompatActivity) getActivity()).createDialog(CompanySearch.newInstance(trendsCompany), "compSearch");
                }
                return true;
            }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    private void populateTrendList(final List<Trend> trends) {
        trendsCompany = trends;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter = new TrendAdapter(trends, getContext(), getActivity());
                trendingList.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                trendingList.setVisibility(View.VISIBLE);
                adapter.notifyItemChanged(0, adapter.getItemCount());
            }
        }, RECYCLER_LOAD_TIME);
    }

    private void bottomNavigationVisibilityControl(int visibilityState) {
        btnNav.setVisibility(visibilityState);
    }

    private void queryListener(Query query) {
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    final List<Trend> list = new ArrayList<>();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        list.add(snapshot.toObject(Trend.class));
                    }
                    if (isVisible() && getContext() != null && list.size() > 0) {
                        populateTrendList(list);
                    }
                }
            }
        });

    }
}
