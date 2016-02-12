package com.sophomoreventure.collegeconnect.Network;

import com.sophomoreventure.collegeconnect.Event;

import java.util.ArrayList;



/**
 * Created by Vikas Kumar on 07-02-2016.
 */
public interface SqlDataListener {

    public void loadData(ArrayList<Event> data);

    public void loadEventById(Event event);
}
