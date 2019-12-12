package com.mrshadat.tourmateapp.serviceapi;

import com.mrshadat.tourmateapp.currentweather.CurrentWeatherResponseBody;
import com.mrshadat.tourmateapp.forecastweather.ForecastWeatherResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceApi {

    @GET()
    Call<CurrentWeatherResponseBody> getCurrentWeather(@Url String endUrl);

    @GET
    Call<ForecastWeatherResponseBody> getForecastWeather(@Url String endUrl);
}
