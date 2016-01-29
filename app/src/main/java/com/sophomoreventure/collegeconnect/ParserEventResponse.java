package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.util.Log;

import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.ModelClass.EventModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public class ParserEventResponse {

    public static   ArrayList<Event> parseEventsJSON(JSONObject response,Context context) {

        ArrayList<Event> listEvents = new ArrayList<>();
        String names[] = new String[2];
        int mobNumbers[] = new int[2];
        EventDatabase eventDatabase = new EventDatabase(context);
        if(response == null || response.length() == 0){
            return null;
        }

        try {
            JSONArray jsonArray = response.getJSONArray("events");
            for(int i = 0; i < jsonArray.length(); i++ ){
                Event event = new Event();
                JSONObject currentEvent = jsonArray.getJSONObject(i);
                String about  = currentEvent.getString("about");
                String clubName = currentEvent.getString("clubname");
                int clubID = currentEvent.getInt("club_id");
                String availabeSeats = currentEvent.getString("available_seats");
                JSONArray contacts = currentEvent.getJSONArray("contacts");

                for(int j = 0; j < contacts.length() ; j++){
                    JSONObject current = contacts.getJSONObject(j);
                    int mobileNo = current.getInt("mobno");
                    String name = current.getString("name");
                    names[j] = name;
                    mobNumbers[j] = mobileNo;
                }
                String eventName = currentEvent.getString("name");
                int createdby = currentEvent.getInt("createdby");
                int totalSeats = currentEvent.getInt("total_seats");
                String venue = currentEvent.getString("venue");
                boolean verified = currentEvent.getBoolean("verified");
                int eventEndTime = currentEvent.getInt("edt");
                int startDateTime = currentEvent.getInt("sdt");
                int occupiedSeats = currentEvent.getInt("occupied_seats");
                int lastRegistrationTime = currentEvent.getInt("lrt");


                event.setEventStarttime(startDateTime);
                event.setEventEndTime(eventEndTime);
                event.setLastRegistrationTime(String.valueOf(lastRegistrationTime));
                event.setEventAttend(occupiedSeats);
                event.setEventTitle(eventName);
                event.setEventDescription(about);
                event.setEventVenue(venue);
                event.setEventClub(clubName);
                event.setEventvarified(verified);
                event.setEventOrganizerOne(names[0]);
                event.setEventOrganizerTwo(names[1]);
                event.setEventOrganizerOnePhoneNo(String.valueOf(mobNumbers[0]));
                event.setEventOrganizerTwoPhoneNo(String.valueOf(mobNumbers[1]));
                listEvents.add(event);
                eventDatabase.insertRow(eventName,0,startDateTime,eventEndTime,occupiedSeats,clubName,about,names[0],names[1]
                    ,venue,String.valueOf(mobNumbers[0]),String.valueOf(mobNumbers[1]),"null","False",false,1,
                        String.valueOf(lastRegistrationTime));
            }


            //eventDatabase.insertData(listEvents,false);

            Log.i("vikas", "parsing done");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEvents;
    }
}
