package com.sophomoreventure.collegeconnect;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Activities.LoginActivity;
import com.sophomoreventure.collegeconnect.Activities.MyIntro;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.Network.ServiceClass;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by Murali on 01/02/2016.
 */
public class SplashActivity extends AppCompatActivity implements DataListener {
    ProgressBar progressBar;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private static final long POLL_FREQUENCY = 28800000;
    private static final int JOB_ID = 100;
    private JobScheduler mJobScheduler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.loadingProgress);
        progressBar.setIndeterminate(true);


        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
        if (isFirstStart) {

            Intent i = new Intent(SplashActivity.this, MyIntro.class);
            startActivity(i);
            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("firstStart", false);
            e.apply();
        }else{
            setupJob();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (EventUtility.isFirstRun(SplashActivity.this) || !EventUtility.isLoggedIn(SplashActivity.this)) {
                        //Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                        Intent intent = new Intent(SplashActivity.this, SlideShowActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        //startActivity(intent);
                        //finish();
                    } else {
                        RequestorGet.requestUserInfo(requestQueue, API.USER_PROFILE_API,
                                EventUtility.getUserTokenFromPref(SplashActivity.this), "None", SplashActivity.this);
                    }
                }
            }, 1500);
        }

    }

    @Override
    public void onDataLoaded(String apiUrl) {
        Log.i("vikas", "in onDataLoded");
        Intent intent = new Intent(SplashActivity.this, SlideShowActivity.class);
        switch (apiUrl) {
            case API.USER_PROFILE_API:
                Log.i("vikas", "case user profile api");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

            case API.USER_LOGIN_API:
                Log.i("vikas", "case user login api");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void setError(String apiUrl, String errorCode) {

        switch (apiUrl) {
            case API.USER_PROFILE_API:
                Log.i("vikas", "in setError user profile api");
                if (errorCode.equals("ERR04")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    EventUtility.removeUserLoginFromPref(this);
                    startActivity(intent);
                }
                if (errorCode.equals("ERR05")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(this, "It seems you changed your password so please login again", Toast.LENGTH_LONG).show();
                    EventUtility.removeUserLoginFromPref(this);
                    startActivity(intent);
                }
                if (errorCode.equals("ERR06")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                if (errorCode.equals("ERR07")) {
                    RequestorGet.requestLogin(requestQueue, API.USER_LOGIN_API,
                            EventUtility.getUserEmailFromPref(this),
                            EventUtility.getUserPasswordHashFromPref(this), this);


                }


                break;

            case API.USER_LOGIN_API:

                Log.i("vikas", "in setError user login api");
                if (errorCode.equals("ERR04")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    EventUtility.removeUserLoginFromPref(this);
                    finish();
                }
                if (errorCode.equals("ERR05")) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(this, "It seems you changed your password so please login again", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    EventUtility.removeUserLoginFromPref(this);
                    finish();
                }
        }

}


    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                buildJob();
            }
        }, 3000);
    }

    private void buildJob() {

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceClass.class));
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }
}
