package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.extras.API;
import com.sophomoreventure.collegeconnect.extras.Constants;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.ModelClass.ClubModel;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public class EventsUtils {

    public static ArrayList<Event> loadEventsData(RequestQueue requestQueue,Context context) {

        RequestorGet.requestEventData(requestQueue, API.EVENT_API,context);
        Log.i("vikas","event request");

        return  null;
    }

    public static ArrayList<ClubModel> loadClubData(RequestQueue requestQueue, Context context) {

        RequestorGet.requestClubData(requestQueue,API.CLUB_LIST_GET_API,context);
        return null;
    }
}
