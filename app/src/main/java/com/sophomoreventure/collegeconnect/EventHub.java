package com.sophomoreventure.collegeconnect;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Murali on 24/12/2015.
 */
public class EventHub {

    static EventHub sEventHub;
    ArrayList<Event> eventList;
    Context mApplicationContext;


    EventHub(Context context) {
        mApplicationContext = context;
        eventList = new ArrayList<Event>();
    }

    public static EventHub getEventHub(Context context) {
        if (sEventHub == null) {
            sEventHub = new EventHub(context.getApplicationContext());
        }
        return sEventHub;
    }


    ArrayList getEventList() {
        return eventList;
    }

    public void addEventToEventList(Event event) {
        eventList.add(event);
    }

    Event getEventForId(UUID uuid) {
        for (Event event : eventList) {
            if (event.getEventId().equals(uuid)) {
                return event;
            }
        }
        return null;
    }
}
