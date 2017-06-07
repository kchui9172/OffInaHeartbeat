package com.example.nikhil.offinaheartbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kchui on 5/3/17.
 */

/**
 * SplashActivity is opening page of app with logo
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Splash screen directs to page to connect smart appliances
        Intent intent = new Intent(this, SmartDeviceSetup.class);
        startActivity(intent);
        finish();
    }
}


