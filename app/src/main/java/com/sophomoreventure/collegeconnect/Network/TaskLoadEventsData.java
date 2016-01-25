package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.EventsLoadedListener;
import com.sophomoreventure.collegeconnect.ModelClass.EventModel;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class TaskLoadEventsData extends AsyncTask<Void, Void, ArrayList<Event>> {

    private EventsLoadedListener myListener;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private Context context;


    public TaskLoadEventsData(Context context){
        this.myListener = (EventsLoadedListener) context;
        volleySingleton = new VolleySingleton(context);
        requestQueue = volleySingleton.getRequestQueue();
        this.context = context;
    }

    @Override
    protected ArrayList<Event> doInBackground(Void... params) {
        // Load events here form api
        ArrayList<Event> listEvents = EventsUtils.loadEventsData(requestQueue,context);
        return listEvents;
    }

    @Override
    protected void onPostExecute(ArrayList<Event> listEvents) {
        if (myListener != null) {
            myListener.onEventsLoaded(listEvents);
        }
    }
}
