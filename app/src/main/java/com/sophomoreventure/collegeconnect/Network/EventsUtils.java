package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.ModelClass.EventModel;
import com.sophomoreventure.collegeconnect.ParserEventResponse;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public class EventsUtils {

    public static ArrayList<Event> loadEventsData(RequestQueue requestQueue,Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);


        RequestorGet.requestEventData(requestQueue, API.EVENT_API,context);

        //ArrayList<Event> listEvents = ParserEventResponse.parseEventsJSON(response);
        //EventDatabase eventDatabase = new EventDatabase(context);
        //eventDatabase.insertData(listEvents,false);

        return  null;
    }
}