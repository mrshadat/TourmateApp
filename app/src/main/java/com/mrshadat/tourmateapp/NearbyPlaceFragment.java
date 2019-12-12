package com.mrshadat.tourmateapp;


import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mrshadat.tourmateapp.viewmodels.LocationViewModel;
import com.mrshadat.tourmateapp.viewmodels.NearbyViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyPlaceFragment extends Fragment {

    private LocationViewModel locationViewModel;
    //private GoogleMap googleMap;
    private Button restaurantBtn, atmBtn;
    private String type = "";
    private NearbyViewModel nearbyViewModel;
    private Location myLocation = null;

    public NearbyPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        locationViewModel =
                ViewModelProviders.of(getActivity())
                        .get(LocationViewModel.class);
        nearbyViewModel =
                ViewModelProviders.of(getActivity())
                        .get(NearbyViewModel.class);
        return inflater.inflate(R.layout.fragment_nearby_place, container, false);
    }

}
