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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sophomoreventure.collegeconnect.Activities.LoginActivity;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.GCM.RegistrationService;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.EventsUtils;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.ServiceClass;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.extras.API;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by Murali on 01/02/2016.
 */
public class SplashActivity extends AppCompatActivity implements DataListener {
    private static final long POLL_FREQUENCY = 28800000;
    private static final int JOB_ID = 100;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "35113555015";
    ProgressBar progressBar;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


    private JobScheduler mJobScheduler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();
        setContentView(R.layout.activity_splash);
        overridePendingTransition(0, 0);
        progressBar = (ProgressBar) findViewById(R.id.loadingProgress);
        progressBar.setIndeterminate(true);
       // setupJob();
        //setupJob();

         EventsUtils.loadEventsData(requestQueue, this);
         EventsUtils.loadClubData(requestQueue,this);
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
        if (isFirstStart) {

            Intent i = new Intent(SplashActivity.this, NewAppIntro.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("firstStart", false);
            e.apply();
        }else{

            final boolean isInternetOn = EventUtility.isNetworkAvailable(this);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (EventUtility.isFirstRun(SplashActivity.this) || !EventUtility.isLoggedIn(SplashActivity.this)) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        if (isInternetOn || checkPlayServices() && EventUtility.getGCMToken(SplashActivity.this) == null) {
                            // Start IntentService to register this application with GCM.
                            Intent gcmIntent = new Intent(SplashActivity.this, RegistrationService.class);
                            startService(gcmIntent);
                        } else {
                            Log.i("tag", "No valid Google Play Services APK found.");

                        }
//                    Intent intent = new Intent(SplashActivity.this, SlideShowActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        startActivity(intent);
                        finish();
                    } else {
                        if (isInternetOn) {
                            if(EventUtility.getGCMToken(SplashActivity.this) == null){
                                Intent gcmIntent = new Intent(SplashActivity.this, RegistrationService.class);
                                startService(gcmIntent);
                            }
                            RequestorGet.requestUserInfo(requestQueue, API.USER_PROFILE_API,
                                    EventUtility.getUserTokenFromPref(SplashActivity.this), "None", SplashActivity.this);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, SlideShowActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    }
                }
            }, 1000);

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
                RequestorGet.requestUserInfo(requestQueue, API.USER_PROFILE_API,
                        EventUtility.getUserTokenFromPref(SplashActivity.this), "None", SplashActivity.this);
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
                if (errorCode.equalsIgnoreCase("NOCON")) {
                    Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, SlideShowActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
                if (errorCode.equalsIgnoreCase("NOCON")) {
                    Toast.makeText(SplashActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
        }

}

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("tag", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                buildJob();
            }
        }, 2000);
    }

    private void buildJob() {

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceClass.class));
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }
}
