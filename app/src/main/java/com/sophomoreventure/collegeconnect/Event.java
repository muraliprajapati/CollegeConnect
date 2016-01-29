package com.sophomoreventure.collegeconnect;

import java.util.UUID;

/**
 * Created by Murali on 24/12/2015.
 */
public class Event {


    private UUID eventId;
    private String eventTitle;
    private String EventClub;
    private String eventDescription;
    private long eventTime;
    private String eventVenue;
    private String eventOrganizerOne;
    private String eventOrganizerOnePhoneNo;
    private String eventOrganizerTwo;
    private String eventOrganizerTwoPhoneNo;
    private long[] notificationDateTime = new long[5];
    private boolean isAdmin;
    private int EventAttend;
    private long EventStarttime;
    private long EventEndTime;
    private boolean Eventvarified;
    private int eventServerId;
    private String lastRegistrationTime;
    private String organizerEmail;

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public int getEventServerId() {
        return eventServerId;
    }

    public void setEventServerId(int eventServerId) {
        this.eventServerId = eventServerId;
    }

    public String getLastRegistrationTime() {
        return lastRegistrationTime;
    }

    public void setLastRegistrationTime(String lastRegistrationTime) {
        this.lastRegistrationTime = lastRegistrationTime;
    }

    public Event() {
        eventId = UUID.randomUUID();
        eventTitle = "null";
        EventClub = "null";
        eventDescription = "null";
        eventTime = 0;
        eventVenue = "null";
        eventOrganizerOne = "null";
        eventOrganizerOnePhoneNo = "null";
        eventOrganizerTwo = "null";
        eventOrganizerTwoPhoneNo = "null";
        initNotificationDateTime(notificationDateTime);
        EventAttend = 0;
        EventStarttime = 0;
        EventEndTime = 0;
        Eventvarified = false;
        organizerEmail = "null";
    }
    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public long[] getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(long[] notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getEventAttend() {
        return EventAttend;
    }

    public void setEventAttend(int eventAttend) {
        EventAttend = eventAttend;
    }

    public long getEventStarttime() {
        return EventStarttime;
    }

    public void setEventStarttime(long eventStarttime) {
        EventStarttime = eventStarttime;
    }

    public long getEventEndTime() {
        return EventEndTime;
    }

    public void setEventEndTime(long eventEndTime) {
        EventEndTime = eventEndTime;
    }

    public boolean isEventvarified() {
        return Eventvarified;
    }

    public void setEventvarified(boolean eventvarified) {
        Eventvarified = eventvarified;
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

    public String getEventClub() {
        return EventClub;
    }

    public void setEventClub(String eventClub) {
        this.EventClub = eventClub;
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
