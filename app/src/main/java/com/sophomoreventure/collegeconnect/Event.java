package com.sophomoreventure.collegeconnect;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Murali on 24/12/2015.
 */
public class Event {
    Calendar[] notificationDateTimeCalendar = new Calendar[5];
    Calendar calendar;
    private UUID eventId;
    private String eventTitle;
    private String clubOfEvent;
    private String eventDescription;
    private Calendar eventDateAndTime;
    private String eventVenue;
    private String eventOrganizerOne;
    private String eventOrganizerOnePhoneNo;
    private String eventOrganizerTwo;
    private String eventOrganizerTwoPhoneNo;
    private boolean isAdmin;


    public Event() {
        eventId = UUID.randomUUID();
        initNotificationDateTime(notificationDateTimeCalendar);
    }

    public UUID getEventId() {
        return eventId;
    }


    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getClubOfEvent() {
        return clubOfEvent;
    }

    public void setClubOfEvent(String clubOfEvent) {
        this.clubOfEvent = clubOfEvent;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Calendar getEventDateAndTime() {
        return eventDateAndTime;
    }

    public void setEventDateAndTime(Calendar eventDateAndTime) {
        this.eventDateAndTime = eventDateAndTime;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventOrganizerOne() {
        return eventOrganizerOne;
    }

    public void setEventOrganizerOne(String eventOrganizerOne) {
        this.eventOrganizerOne = eventOrganizerOne;
    }

    public String getEventOrganizerOnePhoneNo() {
        return eventOrganizerOnePhoneNo;
    }

    public void setEventOrganizerOnePhoneNo(String eventOrganizerOnePhoneNo) {
        this.eventOrganizerOnePhoneNo = eventOrganizerOnePhoneNo;
    }

    public String getEventOrganizerTwo() {
        return eventOrganizerTwo;
    }

    public void setEventOrganizerTwo(String eventOrganizerTwo) {
        this.eventOrganizerTwo = eventOrganizerTwo;
    }

    public String getEventOrganizerTwoPhoneNo() {
        return eventOrganizerTwoPhoneNo;
    }

    public void setEventOrganizerTwoPhoneNo(String eventOrganizerTwoPhoneNo) {
        this.eventOrganizerTwoPhoneNo = eventOrganizerTwoPhoneNo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Calendar[] getNotificationDateTimeArray() {
        return notificationDateTimeCalendar;
    }

    void addNotificationDateTime(int index, Calendar dateAndTime) {
        calendar = dateAndTime;
        notificationDateTimeCalendar[index] = dateAndTime;
    }

    String getNotificationDateTime(int index) {

        long timeInMillis = notificationDateTimeCalendar[index].getTimeInMillis();
        return EventUtility.getFriendlyDayString(timeInMillis);


    }

    void initNotificationDateTime(Calendar[] array) {
        int year = 1970;
        int month = 0;
        int day = 1;
        Calendar calendar = new GregorianCalendar(year, month, day);
        for (int i = 0; i < array.length; i++) {
            array[i] = calendar;
        }

    }



}
