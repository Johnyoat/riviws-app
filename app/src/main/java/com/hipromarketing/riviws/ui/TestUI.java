package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hipromarketing.riviws.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestUI extends Fragment {

    public static TestUI newInstance() {

        Bundle args = new Bundle();

        TestUI fragment = new TestUI();
        fragment.setArguments(args);
        return fragment;
    }


    public TestUI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.test_ui, container, false);
    }

}
