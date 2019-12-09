package com.mrshadat.tourmateapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private CurrentWeatherFragment currentWeatherFragment;
    private ForcastWeatherFragment forcastWeatherFragment;

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;


    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentWeatherFragment = new CurrentWeatherFragment();
        forcastWeatherFragment = new ForcastWeatherFragment();
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        setFragment(currentWeatherFragment);

        frameLayout = view.findViewById(R.id.weather_frame);
        bottomNavigationView = view.findViewById(R.id.weather_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        setFragment(currentWeatherFragment);
                        return true;

                    case R.id.nav_notif:
                        setFragment(forcastWeatherFragment);
                        return true;

                    default:
                        setFragment(currentWeatherFragment);
                        return false;

                }


            }
        });


    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.weather_frame, fragment);
        fragmentTransaction.commit();

    }

}
