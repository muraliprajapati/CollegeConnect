package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.Event;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public class EventsUtils {

    public static ArrayList<Event> loadEventsData(RequestQueue requestQueue,Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);

        String userName = prefs.getString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY,"null");
        String userPass = prefs.getString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY,"null");

        JSONObject response = RequestorGet.requestJsonDataForEvent(requestQueue, API.EVENT_API, context);
        Log.i("tag", "load event data");
//        Log.i("tag",response.toString());
//        ArrayList<Event> listEvents = ParserEventResponse.parseEventsJSON(response, context);
//        EventDatabase eventDatabase = new EventDatabase(context);
//        eventDatabase.insertData(listEvents,false);

        return null;
    }
}
