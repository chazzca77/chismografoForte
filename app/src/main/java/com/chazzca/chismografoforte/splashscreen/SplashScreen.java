package com.chazzca.chismografoforte.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.chazzca.chismografoforte.LoginActivity;
import com.chazzca.chismografoforte.R;

public class SplashScreen extends AppCompatActivity {

    Long splashTime = 3000L;
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        myHandler = new Handler();

        myHandler.postDelayed(this::goToMainActivity, splashTime);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}