package com.mrshadat.tourmateapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mrshadat.tourmateapp.nearby.NearbyResponseBody;
import com.mrshadat.tourmateapp.repos.NearbyRepository;

public class NearbyViewModel extends ViewModel {

    private NearbyRepository repository;

    public NearbyViewModel(){
        repository = new NearbyRepository();
    }

    public MutableLiveData<NearbyResponseBody> getResponseFromRepo(String endUrl){
        return repository.getResponse(endUrl);
    }
}
