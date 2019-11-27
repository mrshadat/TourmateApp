package com.mrshadat.tourmateapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mrshadat.tourmateapp.repos.FirebaseLoginRepository;

public class LoginViewModel extends ViewModel {
    public enum AuthenticatinState {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    private FirebaseLoginRepository repository;
    public MutableLiveData<AuthenticatinState> stateLiveData;
    public MutableLiveData<String> errMsg = new MutableLiveData<>();

    public LoginViewModel() {
        stateLiveData = new MutableLiveData<>();
        repository = new FirebaseLoginRepository(stateLiveData);
        if (repository.getFirebaseUser() != null) {
            stateLiveData.postValue(AuthenticatinState.AUTHENTICATED);
        } else {
            stateLiveData.postValue(AuthenticatinState.UNAUTHENTICATED);
        }
    }

    public void login(String email, String password) {
        stateLiveData = repository.loginFirebaseUser(email, password);
    }

    public void register(String email, String password) {
        stateLiveData = repository.registerFirebaseUser(email, password);
    }
}
