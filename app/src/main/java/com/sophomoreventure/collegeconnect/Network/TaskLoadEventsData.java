package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.EventsLoadedListener;
import com.sophomoreventure.collegeconnect.ModelClass.ClubModel;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class TaskLoadEventsData extends AsyncTask<Void, Void, ArrayList<Event>> {

    private EventsLoadedListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private Context context;


    public TaskLoadEventsData(Context context) throws NoSuchAlgorithmException, KeyManagementException {
        this.myListener = (EventsLoadedListener) context;
        volleySingleton = VolleySingleton.getInstance(context);
        requestQueue = volleySingleton.getRequestQueue();
        this.context = context;
    }

    @Override
    protected ArrayList<Event> doInBackground(Void... params) {
        // Load events here form api
        Log.i("vikas","event data get request");
        ArrayList<Event> listEvents = EventsUtils.loadEventsData(requestQueue,context);
        ArrayList<ClubModel> listClubData = EventsUtils.loadClubData(requestQueue,context);
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Event> listEvents) {
        if (myListener != null) {
            myListener.onEventsLoaded(listEvents);
        }
    }
}
