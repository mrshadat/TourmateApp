package com.mrshadat.tourmateapp.repos;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mrshadat.tourmateapp.currentweather.CurrentWeatherResponseBody;
import com.mrshadat.tourmateapp.forecastweather.ForecastWeatherResponseBody;
import com.mrshadat.tourmateapp.helper.RetrofitClient;
import com.mrshadat.tourmateapp.serviceapi.WeatherServiceApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    public MutableLiveData<CurrentWeatherResponseBody> currentResponseLD =
            new MutableLiveData<>();
    public MutableLiveData<ForecastWeatherResponseBody> forecastResponseLD =
            new MutableLiveData<>();

    public MutableLiveData<CurrentWeatherResponseBody> getCurrentWeather(Location location, String unit, String apiKey){
        WeatherServiceApi serviceApi =
                RetrofitClient.getClientForWeather()
                        .create(WeatherServiceApi.class);
        String endUrl = String.format("data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                location.getLatitude(),
                location.getLongitude(),
                unit, apiKey);

        serviceApi.getCurrentWeather(endUrl)
                .enqueue(new Callback<CurrentWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponseBody> call, Response<CurrentWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            CurrentWeatherResponseBody responseBody = response.body();
                            currentResponseLD.postValue(responseBody);
                            Log.e(TAG, "onResponse: "+responseBody.getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getLocalizedMessage());
                    }
                });
        return currentResponseLD;

    }

    public MutableLiveData<ForecastWeatherResponseBody> getForecastWeather(Location location, String unit, String apiKey){
        WeatherServiceApi serviceApi =
                RetrofitClient.getClientForWeather()
                        .create(WeatherServiceApi.class);
        String endUrl = String.format("data/2.5/forecast/daily?lat=%f&lon=%f&cnt=7&units=%s&appid=%s",
                location.getLatitude(),
                location.getLongitude(),
                unit, apiKey);
        serviceApi.getForecastWeather(endUrl)
                .enqueue(new Callback<ForecastWeatherResponseBody>() {
                    @Override
                    public void onResponse(Call<ForecastWeatherResponseBody> call, Response<ForecastWeatherResponseBody> response) {
                        if (response.isSuccessful()){
                            forecastResponseLD.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<ForecastWeatherResponseBody> call, Throwable t) {

                    }
                });
        return forecastResponseLD;
    }
}
