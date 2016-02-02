package com.sophomoreventure.collegeconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sophomoreventure.collegeconnect.Activities.LoginActivity;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;

/**
 * Created by Murali on 01/02/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (EventUtility.isFirstRun(SplashActivity.this) || !EventUtility.isLoggedIn(SplashActivity.this)) {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, SlideShowActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            }
        }, 3000);
    }
}
