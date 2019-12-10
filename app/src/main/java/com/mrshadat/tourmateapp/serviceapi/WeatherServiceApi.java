package com.mrshadat.tourmateapp.serviceapi;

import com.mrshadat.tourmateapp.currentweather.CurrentWeatherResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {

    @GET()
    Call<CurrentWeatherResponseBody> getCurrentWeather(@Url String endUrl);
}
