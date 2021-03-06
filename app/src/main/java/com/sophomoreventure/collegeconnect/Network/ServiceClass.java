package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.util.Log;

import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.EventsLoadedListener;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class ServiceClass extends JobService implements EventsLoadedListener{
    Context context;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.jobParameters = jobParameters;
        context = this;
        //L.T(this, "jobDone");
        jobFinished(jobParameters, false);
        Log.i("vikas", "event data get request service above try");
        try {
            Log.i("vikas", "event data get request service");
            new TaskLoadEventsData(context).execute();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


    @Override
    public void onEventsLoaded(ArrayList<Event> listEvents) {
        jobFinished(jobParameters, false);
    }
}


