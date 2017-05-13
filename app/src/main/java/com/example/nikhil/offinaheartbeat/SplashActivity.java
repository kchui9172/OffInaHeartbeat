package com.example.nikhil.offinaheartbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kchui on 5/3/17.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = new Intent(this, MainActivity.class);

        //Splash screen directs to page to connect smart appliances
        Intent intent = new Intent(this, SmartDeviceSetup.class);
        startActivity(intent);
        finish();
    }
}

