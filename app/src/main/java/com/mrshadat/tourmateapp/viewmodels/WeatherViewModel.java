package com.mrshadat.tourmateapp.viewmodels;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mrshadat.tourmateapp.currentweather.CurrentWeatherResponseBody;
import com.mrshadat.tourmateapp.repos.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    private static final String TAG = WeatherViewModel.class.getSimpleName();
    private WeatherRepository repository;

    public WeatherViewModel(){
        repository = new WeatherRepository();
    }

    public MutableLiveData<CurrentWeatherResponseBody> getCurrentWeather(Location location,
                                                                         String unit,
                                                                         String apikey){
        return repository.getCurrentWeather(location, unit, apikey);
        /*Log.e(TAG, "getCurrentWeather: "+
                currentResponseLD.getValue()
        .getName());*/
    }
}
