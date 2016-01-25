package com.sophomoreventure.collegeconnect;

import com.sophomoreventure.collegeconnect.ModelClass.EventModel;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 16-01-2016.
 */
public interface EventsLoadedListener {
    public void onEventsLoaded(ArrayList<Event> listEvents);
}
