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
    private String eventTime;
    private String eventVenue;
    private String eventOrganizerOne;
    private String eventOrganizerOnePhoneNo;
    private String eventOrganizerTwo;
    private String eventOrganizerTwoPhoneNo;
    private long[] notificationDateTime = new long[5];
    private String isAdmin;
    private String EventAttend;
    private String eventLiked;
    private String EventStarttime;
    private String EventEndTime;
    private String Eventvarified;
    private String eventServerId;
    private String lastRegistrationTime;
    private String organizerEmailOne;
    private String organizerEmailTwo;
    private String UrlThumbnail;

    public String getOrganizerEmailOne() {
        return organizerEmailOne;
    }

    public void setOrganizerEmailOne(String organizerEmailOne) {
        this.organizerEmailOne = organizerEmailOne;
    }

    public String getOrganizerEmailTwo() {
        return organizerEmailTwo;
    }

    public void setOrganizerEmailTwo(String organizerEmailTwo) {
        this.organizerEmailTwo = organizerEmailTwo;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getEventAttend() {
        return EventAttend;
    }

    public void setEventAttend(String eventAttend) {
        EventAttend = eventAttend;
    }

    public String getEventStarttime() {
        return EventStarttime;
    }

    public void setEventStarttime(String eventStarttime) {
        EventStarttime = eventStarttime;
    }

    public String getEventEndTime() {
        return EventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        EventEndTime = eventEndTime;
    }

    public String getEventvarified() {
        return Eventvarified;
    }

    public void setEventvarified(String eventvarified) {
        Eventvarified = eventvarified;
    }

    public String getEventServerId() {
        return eventServerId;
    }

    public void setEventServerId(String eventServerId) {
        this.eventServerId = eventServerId;
    }

    public String getLastRegistrationTime() {
        return lastRegistrationTime;
    }

    public void setLastRegistrationTime(String lastRegistrationTime) {
        this.lastRegistrationTime = lastRegistrationTime;
    }

    public String getEventLiked() {
        return eventLiked;
    }

    public void setEventLiked(String eventLiked) {
        this.eventLiked = eventLiked;
    }

    public Event() {
        eventId = UUID.randomUUID();
        eventTitle = "null";
        EventClub = "null";
        eventDescription = "null";
        eventTime = "null";
        eventVenue = "null";
        eventOrganizerOne = "null";
        eventOrganizerOnePhoneNo = "null";
        eventOrganizerTwo = "null";
        eventOrganizerTwoPhoneNo = "null";
        eventLiked = "false";
        initNotificationDateTime(notificationDateTime);
        EventAttend = "null";
        EventStarttime = "null";
        EventEndTime = "null";
        Eventvarified = "null";
        organizerEmailOne = "null";
        organizerEmailTwo = "null";
        UrlThumbnail = "null";
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

    public String getUrlThumbnail() {
        return UrlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        UrlThumbnail = urlThumbnail;
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
