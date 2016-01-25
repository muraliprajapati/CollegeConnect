package com.sophomoreventure.collegeconnect;

import com.sophomoreventure.collegeconnect.ModelClass.EventModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public class ParserEventResponse {
    public static ArrayList<Event> parseEventsJSON(JSONObject response) {

        ArrayList<Event> listEvents = new ArrayList<>();
        String names[] = {};
        int mobNumbers[] = {};

        if(response == null || response.length() == 0){
            return null;
        }

        try {
            JSONArray jsonArray = response.getJSONArray("events");
            for(int i = 0; i < jsonArray.length(); i++ ){
                Event event = new Event();
                JSONObject currentEvent = jsonArray.getJSONObject(i);
                String about  = currentEvent.getString("about");
                String availabeSeats = currentEvent.getString("available_seats");
                JSONArray contacts = currentEvent.getJSONArray("contacts");

                for(int j = 0; j < contacts.length() ; j++){
                    JSONObject current = contacts.getJSONObject(j);
                    int mobileNo = current.getInt("mobno");
                    String name = current.getString("name");
                    names[j] = name;
                    mobNumbers[j] = mobileNo;
                }

                String createdby = currentEvent.getString("createdby");
                int totalSeats = currentEvent.getInt("total_seats");
                String vanue = currentEvent.getString("venue");
                boolean varified = currentEvent.getBoolean("verified");

                event.setEventDescription(about);
                event.setEventVenue(vanue);
                event.setEventvarified(varified);
                event.setEventOrganizerOne(names[0]);
                event.setEventOrganizerTwo(names[1]);
                event.setEventOrganizerOnePhoneNo(String.valueOf(mobNumbers[0]));
                event.setEventOrganizerTwoPhoneNo(String.valueOf(mobNumbers[1]));
                listEvents.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEvents;
    }
}
