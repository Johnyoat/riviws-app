package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hipromarketing.riviws.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePreview extends DialogFragment {

    public static ImagePreview newInstance(String image) {

        Bundle args = new Bundle();

        ImagePreview fragment = new ImagePreview();
        args.putString("image", image);
        fragment.setArguments(args);
        return fragment;
    }


    public ImagePreview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.image_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatImageView image = view.findViewById(R.id.image);
        AppCompatImageButton close = view.findViewById(R.id.close);


        Glide.with(getContext()).load(getArguments().getString("image")).into(image);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
    }
}
