package com.luvina.democalendar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.luvina.democalendar.R;

/**
 * Class handling Splash screen
 */
public class SplashActivity extends AppCompatActivity {
    // Time delay for Splash screen
    private int delayMillis = 3000;

    /**
     * Create Splash Screen
     *
     * @param savedInstanceState: object contains the most recent data
     * @author HoangNN
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, delayMillis);
    }
}
