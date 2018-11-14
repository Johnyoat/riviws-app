package com.hipromarketing.riviws.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hipromarketing.riviws.R;
import com.hipromarketing.riviws.adapters.CompanySearchAdapter;
import com.hipromarketing.riviws.models.Company;
import com.hipromarketing.riviws.models.Trend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanySearch extends DialogFragment {
    AppCompatEditText search;
    RecyclerView companyList;
    CompanySearchAdapter adapter;
    RelativeLayout banner;
    AppCompatImageView bck;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static CompanySearch newInstance() {

        Bundle args = new Bundle();

        CompanySearch fragment = new CompanySearch();
        fragment.setArguments(args);
        return fragment;
    }


    public static CompanySearch newInstance(List<Trend> trends) {

        Bundle args = new Bundle();

        CompanySearch fragment = new CompanySearch();
        fragment.setArguments(args);
        args.putParcelableArrayList("trends", (ArrayList<? extends Parcelable>) trends);
        return fragment;
    }

    public CompanySearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View searchLayout = view.findViewById(R.id.searchLayout);

        search = searchLayout.findViewById(R.id.search);
        banner = view.findViewById(R.id.banner);

        companyList = view.findViewById(R.id.companyList);
        companyList.setLayoutManager(new LinearLayoutManager(getContext()));
        bck = view.findViewById(R.id.back);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        final List<Company> companies = new ArrayList<>();


        db.collection("riviws").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null ) {
                    for (DocumentSnapshot snapshot :
                            queryDocumentSnapshots) {
                        companies.add(snapshot.toObject(Trend.class).getCompany());

                        if (companies.size() > 0){
                            adapter = new CompanySearchAdapter((AppCompatActivity) getActivity(), getContext(), companies);
                            companyList.setAdapter(adapter);
                        }
                    }
                }
            }
        });






        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                    if (s.toString().isEmpty()){
                        banner.setVisibility(View.VISIBLE);
                        companyList.setVisibility(View.GONE);
                    }else {
                        banner.setVisibility(View.GONE);
                        companyList.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
