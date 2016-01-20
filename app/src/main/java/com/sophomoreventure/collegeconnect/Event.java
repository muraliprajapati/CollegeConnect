package com.sophomoreventure.collegeconnect;

import java.util.UUID;

/**
 * Created by Murali on 24/12/2015.
 */
public class Event {


    private UUID eventId;
    private String eventTitle;
    private String clubOfEvent;
    private String eventDescription;
    private long eventTime;
    private String eventVenue;
    private String eventOrganizerOne;
    private String eventOrganizerOnePhoneNo;
    private String eventOrganizerTwo;
    private String eventOrganizerTwoPhoneNo;
    private long[] notificationDateTime = new long[5];
    private boolean isAdmin;


    public Event() {
        eventId = UUID.randomUUID();
        eventTitle = "";
        clubOfEvent = "";
        eventDescription = "";
        eventTime = 0;
        eventVenue = "";
        eventOrganizerOne = "";
        eventOrganizerOnePhoneNo = "";
        eventOrganizerTwo = "";
        eventOrganizerTwoPhoneNo = "";
        initNotificationDateTime(notificationDateTime);
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

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
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

    public long[] getNotificationDateTimeArray() {
        return notificationDateTime;
    }

    void addNotificationDateTime(int index, long dateAndTime) {

        notificationDateTime[index] = dateAndTime;
    }

    String getNotificationDateTime(int index) {

        long timeInMillis = notificationDateTime[index];
        return EventUtility.getFriendlyDayString(timeInMillis);


    }

    void initNotificationDateTime(long[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }

    }


}
