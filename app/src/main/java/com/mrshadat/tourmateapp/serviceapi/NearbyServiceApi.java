package com.mrshadat.tourmateapp.serviceapi;

import com.mrshadat.tourmateapp.nearby.NearbyResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyServiceApi {

    @GET
    Call<NearbyResponseBody> getNearbyResponse(@Url String endUrl);
}
