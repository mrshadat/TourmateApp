package com.mrshadat.tourmateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mrshadat.tourmateapp.viewmodels.LoginViewModel;

public class NavDrawerActivity extends AppCompatActivity {

    private TextView statusTv;
    private EditText emailEt, passwordEt;
    private Button loginBtn, registerBtn;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        statusTv = findViewById(R.id.statusTv);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                loginViewModel.login(email, password);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                loginViewModel.register(email, password);
            }
        });

        loginViewModel.stateLiveData.observe(this, new Observer<LoginViewModel.AuthenticatinState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticatinState authenticatinState) {
                switch (authenticatinState) {
                    case AUTHENTICATED:
                        Intent intent = new Intent(NavDrawerActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case UNAUTHENTICATED:

                        break;
                }
            }
        });

        loginViewModel.errMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                statusTv.setText(s);
            }
        });
    }
}
