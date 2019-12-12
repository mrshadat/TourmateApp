package com.mrshadat.tourmateapp;


import android.location.Address;
import android.location.Geocoder;
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

import com.mrshadat.tourmateapp.currentweather.CurrentWeatherResponseBody;
import com.mrshadat.tourmateapp.databinding.FragmentCurrentWeatherBinding;
import com.mrshadat.tourmateapp.helper.EventUtils;
import com.mrshadat.tourmateapp.viewmodels.LocationViewModel;
import com.mrshadat.tourmateapp.viewmodels.WeatherViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {
    private static final String TAG = WeatherFragment.class.getSimpleName();
    private FragmentCurrentWeatherBinding binding;
    private LocationViewModel locationViewModel;
    private WeatherViewModel weatherViewModel;
    private String unit = "metric";

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCurrentWeatherBinding.inflate(LayoutInflater.from(getActivity()));
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
                initializeCurrentWeather(location);
                /*try {
                    convertLatLngToStreetAddress(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                binding.addressTV.setText(location.getLatitude() + " " + location.getLongitude());
            }
        });
    }

    private void initializeCurrentWeather(final Location location) {
        String apikey = getString(R.string.weather_api_key);
        weatherViewModel.getCurrentWeather(location, unit, apikey)
                .observe(this, new Observer<CurrentWeatherResponseBody>() {
                    @Override
                    public void onChanged(CurrentWeatherResponseBody response) {
                        double temp = response.getMain().getTemp();
                        String date = EventUtils.getFormattedDate(response.getDt());
                        String city = response.getName();
                        String icon = response.getWeather().get(0).getIcon();
                        Picasso.get().load(EventUtils.WEATHER_CONDITION_ICON_PREFIX + icon + ".png")
                                .into(binding.weatherIcon);

                        binding.latLngTV.setText(temp + "\n" + date + "\n" + city);

                    }
                });
    }

    private void convertLatLngToStreetAddress(Location location) throws IOException {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                location.getLongitude(), 1);
        String address = addressList.get(0).getAddressLine(0);
        binding.addressTV.setText(address);

    }
}
