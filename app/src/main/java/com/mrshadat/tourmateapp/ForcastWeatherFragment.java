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
import android.widget.Toast;

import com.mrshadat.tourmateapp.databinding.FragmentForcastWeatherBinding;
import com.mrshadat.tourmateapp.forecastweather.ForecastList;
import com.mrshadat.tourmateapp.forecastweather.ForecastWeatherResponseBody;
import com.mrshadat.tourmateapp.viewmodels.LocationViewModel;
import com.mrshadat.tourmateapp.viewmodels.WeatherViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForcastWeatherFragment extends Fragment {

    private FragmentForcastWeatherBinding binding;
    private LocationViewModel locationViewModel;
    private WeatherViewModel weatherViewModel;
    private Location currentlocation;
    private String unit = "metric";

    public ForcastWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentForcastWeatherBinding.inflate(LayoutInflater.from(getActivity()));
        locationViewModel =
                ViewModelProviders.of(getActivity())
                        .get(LocationViewModel.class);
        weatherViewModel =
                ViewModelProviders.of(this)
                        .get(WeatherViewModel.class);
        locationViewModel.getDeviceCurrentLocation();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationViewModel.locationLD.observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                currentlocation = location;
                initializeForecastWeather(location);
            }
        });
    }

    private void initializeForecastWeather(Location location) {
        String apikey = getString(R.string.weather_api_key2);
        weatherViewModel.getForecast(location, unit, apikey)
                .observe(this, new Observer<ForecastWeatherResponseBody>() {
                    @Override
                    public void onChanged(ForecastWeatherResponseBody response) {
                        List<ForecastList> forecastLists = response.getList();
                        Toast.makeText(getContext(), "" + forecastLists.size(), Toast.LENGTH_SHORT).show();
                        String temp = "";
                        for (ForecastList fl : forecastLists) {
                            temp += fl.getTemp().getMax() + "," + fl.getTemp().getMin() + "\n";
                        }
                        binding.forcastTv.setText(temp);
                    }
                });
    }
}
