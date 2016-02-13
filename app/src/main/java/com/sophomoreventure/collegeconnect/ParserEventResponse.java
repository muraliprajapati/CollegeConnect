package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.util.Log;

import com.sophomoreventure.collegeconnect.JsonHandler.Utils;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.extras.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_AVAILABLE_SEATS;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_COLOR;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_CONTACTS;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_CONTACT_MOBNO;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_CONTACT_NAME;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_CREATEDBY;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EDT;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EVENTS;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EVENT_ABOUT;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EVENT_CLUB_ID;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EVENT_CLUB_NAME;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EVENT_ID;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_EVENT_NAME;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_IMAGE_URL;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_LRT;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_OCCUPIED_SEATS;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_STD;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_TOTAL_SEATS;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_VARIFIED;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointEvents.KEY_VENUE;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public class ParserEventResponse {

    public static   ArrayList<Event> parseEventsJSON(JSONObject response,Context context) {

        ArrayList<Event> listEvents = new ArrayList<>();
        String names[] = new String[2];
        String mobNumbers[] = new String[2];
        String about  = Constants.NA;
        String clubName = Constants.NA;
        String clubID = Constants.NA;
        String availabeSeats = Constants.NA;
        String eventName = Constants.NA;
        String createdby = Constants.NA;
        String totalSeats = Constants.NA;
        String venue = Constants.NA;
        String verified = Constants.NA;
        String eventEndTime = Constants.NA;
        String startDateTime = Constants.NA;
        String occupiedSeats = Constants.NA;
        String lastRegistrationTime = Constants.NA;
        String ImageURL = Constants.NA;
        String eventId = Constants.NA;
        String eventColor = Constants.NA;

//        EventDatabase eventDatabase = new EventDatabase(context);
        EventDatabase eventDatabase = EventDatabase.newInstance(context);
        eventDatabase.deleteDatabase();
        if(response == null || response.length() == 0){
            return null;
        }

        try {
            JSONArray jsonArray = response.getJSONArray(KEY_EVENTS);

            for(int i = 0; i < jsonArray.length(); i++ ){
                Event event = new Event();
                JSONObject currentEvent = jsonArray.getJSONObject(i);

                if (Utils.contains(currentEvent, KEY_EVENT_ABOUT)) {
                    about  = currentEvent.getString(KEY_EVENT_ABOUT);
                }

                if (Utils.contains(currentEvent, KEY_EVENT_CLUB_NAME)) {
                    clubName = currentEvent.getString(KEY_EVENT_CLUB_NAME);
                }

                if (Utils.contains(currentEvent, KEY_EVENT_CLUB_ID)) {
                    clubID = String.valueOf(currentEvent.getInt(KEY_EVENT_CLUB_ID));
                }

                if (Utils.contains(currentEvent, KEY_AVAILABLE_SEATS)) {
                    //availabeSeats = currentEvent.getString(KEY_AVAILABLE_SEATS);
                }

                JSONArray contacts = currentEvent.getJSONArray(KEY_CONTACTS);

                for(int j = 0; j < contacts.length() ; j++){

                    JSONObject current = contacts.getJSONObject(j);
                    if (Utils.contains(current, KEY_CONTACT_MOBNO)) {
                        mobNumbers[j] =current.getString(KEY_CONTACT_MOBNO);
                    }

                    if (Utils.contains(current, KEY_CONTACT_NAME)) {
                        names[j] = current.getString(KEY_CONTACT_NAME);
                    }
                }

                if (Utils.contains(currentEvent, KEY_EVENT_NAME)) {
                    eventName = currentEvent.getString(KEY_EVENT_NAME);
                }

                if (Utils.contains(currentEvent, KEY_CREATEDBY)) {
                    createdby = String.valueOf(currentEvent.getLong(KEY_CREATEDBY));
                }

                if (Utils.contains(currentEvent, KEY_TOTAL_SEATS)) {
                    totalSeats = String.valueOf(currentEvent.getInt(KEY_TOTAL_SEATS));
                }

                if (Utils.contains(currentEvent, KEY_VENUE)) {
                    venue = currentEvent.getString(KEY_VENUE);
                }

                if (Utils.contains(currentEvent, KEY_VARIFIED)) {
                    verified = String.valueOf(currentEvent.getBoolean(KEY_VARIFIED));
                }

                if (Utils.contains(currentEvent, KEY_EDT)) {
                    eventEndTime = String.valueOf(currentEvent.getInt(KEY_EDT));
                }

                if (Utils.contains(currentEvent, KEY_STD)) {
                    startDateTime = String.valueOf(currentEvent.getLong(KEY_STD)*1000);
                }

                if (Utils.contains(currentEvent, KEY_OCCUPIED_SEATS)) {
                    occupiedSeats = String.valueOf(currentEvent.getInt(KEY_OCCUPIED_SEATS));
                }

                if (Utils.contains(currentEvent, KEY_LRT)) {
                    lastRegistrationTime = String.valueOf(currentEvent.getInt(KEY_LRT));
                }

                if (Utils.contains(currentEvent, KEY_IMAGE_URL)) {
                    ImageURL = currentEvent.getString(KEY_IMAGE_URL);
                }

                if(Utils.contains(currentEvent, KEY_EVENT_ID)){
                    eventId = String.valueOf(currentEvent.getInt(KEY_EVENT_ID));
                }

                if(Utils.contains(currentEvent,KEY_COLOR)){
                    eventColor = currentEvent.getString(KEY_COLOR);
                }

                event.setEventStarttime(String.valueOf(startDateTime));
                event.setEventEndTime(String.valueOf(eventEndTime));
                event.setLastRegistrationTime(String.valueOf(lastRegistrationTime));
                event.setEventAttend(String.valueOf(occupiedSeats));
                event.setEventTitle(eventName);
                event.setEventDescription(about);
                event.setEventVenue(venue);
                event.setEventClub(clubName);
                event.setEventvarified(String.valueOf(verified));
                event.setEventOrganizerOne(names[0]);
                event.setEventOrganizerTwo(names[1]);
                event.setEventOrganizerOnePhoneNo(String.valueOf(mobNumbers[0]));
                event.setEventOrganizerTwoPhoneNo(String.valueOf(mobNumbers[1]));
                listEvents.add(event);
                eventDatabase.insertRow(eventName, eventColor, startDateTime, eventEndTime, occupiedSeats, clubName, about, names[0], names[1]
                        , venue, String.valueOf(mobNumbers[0]), String.valueOf(mobNumbers[1]), "false", verified, clubID, eventId,
                        String.valueOf(lastRegistrationTime), createdby,ImageURL);
                Log.i("vikas", "Event parsing done  "+ eventName);
            }

            //eventDatabase.insertData(listEvents,false);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("vikas","Json event catch Caluse"+e);
        }
        return listEvents;
    }
}
