package com.hipromarketing.riviws.ui;


import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hipromarketing.riviws.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowLocation extends DialogFragment implements OnMapReadyCallback {
    private static View view;
    private GoogleMap map;
    private Marker marker;
    public static ShowLocation newInstance(String lng,String lat) {

        Bundle args = new Bundle();

        ShowLocation fragment = new ShowLocation();
        args.putString("lng",lng);
        args.putString("lat",lat);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.show_location, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(ShowLocation.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng location = new LatLng(Double.valueOf(getArguments().getString("lat")), Double.valueOf(getArguments().getString("lng")));
        marker = map.addMarker(new MarkerOptions().position(location).title("Here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.setMinZoomPreference(18.0f);
        map.setMaxZoomPreference(14.0f);
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
    }


    @Override
    public void onStop() {
        super.onStop();
        dismissAllowingStateLoss();
        if (marker != null){
            marker.remove();
        }
    }


}
