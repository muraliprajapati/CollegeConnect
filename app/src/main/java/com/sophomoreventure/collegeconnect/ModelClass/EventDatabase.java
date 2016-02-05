package com.sophomoreventure.collegeconnect.ModelClass;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sophomoreventure.collegeconnect.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikas Kumar on 06-01-2016.
 */
public class EventDatabase {

    EventDataBaseHelper helper;
    private Context context;

    public EventDatabase(Context context) {
        helper = new EventDataBaseHelper(context);
        this.context = context;
    }

    public void insertData(ArrayList<Event> listData, boolean clearPrevious) {

        if (clearPrevious) {
            deleteDatabase();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.i("vikas", listData.size() + "inserted list of size : ");
        for (int i = 0; i < listData.size(); i++) {
            Event event = listData.get(i);
            contentValues.put(EventDataBaseHelper.EventName, event.getEventTitle());
            contentValues.put(EventDataBaseHelper.EventDate, event.getEventTime());
            contentValues.put(EventDataBaseHelper.EventStarttime, event.getEventStarttime());
            contentValues.put(EventDataBaseHelper.EventEndTime, event.getEventEndTime());
            contentValues.put(EventDataBaseHelper.EventAttend, event.getEventAttend());
            contentValues.put(EventDataBaseHelper.EventClub, event.getEventClub());
            contentValues.put(EventDataBaseHelper.EventDescription, event.getEventDescription());
            contentValues.put(EventDataBaseHelper.EventoganizernameFirst, event.getEventOrganizerOne());
            contentValues.put(EventDataBaseHelper.EventoganizernameSecond, event.getEventOrganizerTwo());
            contentValues.put(EventDataBaseHelper.EventVanue, event.getEventVenue());
            contentValues.put(EventDataBaseHelper.OrganizerMobFirst, event.getEventOrganizerOnePhoneNo());
            contentValues.put(EventDataBaseHelper.OrganizerMobSecond, event.getEventOrganizerTwoPhoneNo());
            contentValues.put(EventDataBaseHelper.EventServerID, event.getEventServerId());
            contentValues.put(EventDataBaseHelper.LastRegistrationTime, event.getLastRegistrationTime());
            contentValues.put(EventDataBaseHelper.EventVarified, event.getEventvarified());
            contentValues.put(EventDataBaseHelper.IsAdmin, event.getIsAdmin());
            contentValues.put(EventDataBaseHelper.OrganizerEmail, event.getOrganizerEmailOne());
            db.insert(EventDataBaseHelper.Tablename, null, contentValues);
        }

    }

    public void insertRow(String eventname, String eventdate, String eventstarttime,
                          String eventendtime, String eventattend, String eventclub,
                          String eventdescription, String eventfirstorganizer, String eventsecondorganizer,
                          String eventVanue, String organiizermobfirst, String organiizermobsecond, String organizeremail,
                          String eventvarified, String isAdmin, String serverID, String lastRegistrationTime,String eventLiked) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventName, eventname);
        contentValues.put(EventDataBaseHelper.EventDate, eventdate);
        contentValues.put(EventDataBaseHelper.EventStarttime, eventstarttime);
        contentValues.put(EventDataBaseHelper.EventEndTime, eventendtime);
        contentValues.put(EventDataBaseHelper.EventAttend, eventattend);
        contentValues.put(EventDataBaseHelper.EventClub, eventclub);
        contentValues.put(EventDataBaseHelper.EventLiked,eventLiked);
        contentValues.put(EventDataBaseHelper.EventDescription, eventdescription);
        contentValues.put(EventDataBaseHelper.EventoganizernameFirst, eventfirstorganizer);
        contentValues.put(EventDataBaseHelper.EventoganizernameSecond, eventsecondorganizer);
        contentValues.put(EventDataBaseHelper.EventVanue, eventVanue);
        contentValues.put(EventDataBaseHelper.OrganizerMobFirst, organiizermobfirst);
        contentValues.put(EventDataBaseHelper.OrganizerMobSecond, organiizermobsecond);
        contentValues.put(EventDataBaseHelper.OrganizerEmail, organizeremail);
        contentValues.put(EventDataBaseHelper.EventVarified, eventvarified);
        contentValues.put(EventDataBaseHelper.IsAdmin, isAdmin);
        contentValues.put(EventDataBaseHelper.EventServerID, serverID);
        contentValues.put(EventDataBaseHelper.LastRegistrationTime, lastRegistrationTime);
        db.insert(EventDataBaseHelper.Tablename, null, contentValues);

    }

    public void setAttendEvent(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventLiked, "true");
        String[] selecArgs = {id};
        db.update(EventDataBaseHelper.Tablename, contentValues,
                EventDataBaseHelper.EventAttend + " =?", selecArgs);
    }

    public void setNotAttendEvent(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventAttend, "false");
        String[] selecArgs = {id};
        db.update(EventDataBaseHelper.Tablename, contentValues,
                EventDataBaseHelper.EventAttend + " =?", selecArgs);
    }


    public Event selectByEventId(String eventID){
        SQLiteDatabase db = helper.getWritableDatabase();
        Event event = null;
        String[] whereArgs = {eventID};
        Cursor cursor = db.query(EventDataBaseHelper.Tablename, null, EventDataBaseHelper.EventServerID + " =?", whereArgs,
                null, null, null, null);

        while (cursor.moveToNext()) {
            event = new Event();
            event.setEventTitle(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDescription)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventAttend)));
            //event.setEventLiked(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventLiked)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDate)));
            event.setEventVenue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventOrganizerOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameFirst)));
            event.setEventOrganizerTwo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameSecond)));
            event.setOrganizerEmailOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setEventOrganizerOnePhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobFirst)));
            event.setEventOrganizerTwoPhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobSecond)));
            event.setEventvarified((cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified))));
            event.setIsAdmin(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.IsAdmin)));
            event.setEventServerId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventServerID)));
            event.setLastRegistrationTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.LastRegistrationTime)));
        }
        return event;

    }

    public ArrayList<Event> selectByClub(String club) {
        ArrayList<Event> eventList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String query = "SELECT * FROM " + EventDataBaseHelper.Tablename + " WHERE " +
                EventDataBaseHelper.EventClub + "='" + club + "';";
        String whereClause = EventDataBaseHelper.EventClub + "='" + club + "';";
        String whereClause1 = EventDataBaseHelper.EventClub + "=?";
        String[] whereArgs = {club};
        Cursor cursor = db.query(EventDataBaseHelper.Tablename, null, EventDataBaseHelper.EventClub+" =?",whereArgs,
                null, null,null,null);

        while (cursor.moveToNext()) {
            Event event = new Event();
            int index1 = cursor.getColumnIndex(EventDataBaseHelper.UID);
            int id = cursor.getInt(index1);
            event.setEventTitle(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDescription)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventAttend)));
            //event.setEventLiked(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventLiked)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDate)));
            event.setEventVenue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventOrganizerOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameFirst)));
            event.setEventOrganizerTwo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameSecond)));
            event.setOrganizerEmailOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setEventOrganizerOnePhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobFirst)));
            event.setEventOrganizerTwoPhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobSecond)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified)));
            event.setIsAdmin(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.IsAdmin)));
            event.setEventServerId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventServerID)));
            event.setLastRegistrationTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.LastRegistrationTime)));
            eventList.add(event);
        }
        cursor.close();
        return eventList;
    }

    public ArrayList<EventModel> viewAllData() {

        ArrayList<EventModel> eventList = new ArrayList<>();
        String[] columns = {EventDataBaseHelper.UID, EventDataBaseHelper.EventName,
                EventDataBaseHelper.EventDate, EventDataBaseHelper.EventStarttime,
                EventDataBaseHelper.EventEndTime, EventDataBaseHelper.EventAttend,
                EventDataBaseHelper.EventDescription, EventDataBaseHelper.EventoganizernameFirst,
                EventDataBaseHelper.EventVanue, EventDataBaseHelper.OrganizerMobFirst,
                EventDataBaseHelper.OrganizerEmail, EventDataBaseHelper.EventVarified};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(EventDataBaseHelper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EventModel event = new EventModel();
            int index1 = cursor.getColumnIndex(EventDataBaseHelper.UID);
            int id = cursor.getInt(index1);
            event.setEventName(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDescription)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventAttend)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventDate(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDate)));
            event.setEventVanue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventoganizername(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameFirst)));
            event.setOrganizerEmail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setOrganizerMob(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobFirst)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified)));
            eventList.add(event);
        }
        cursor.close();
        Log.i("vikas", eventList.size() + " all");
        return eventList;
    }

    public int deleteRow(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(EventDataBaseHelper.Tablename, EventDataBaseHelper.EventName + " =?", whereArgs);
        return count;
    }

    public void deleteDatabase() {
        //mDatabase.delete(UserDataBaseHelper.Tablename,null,null);
        context.deleteDatabase(EventDataBaseHelper.DataBaseName);

    }

    public class EventDataBaseHelper extends SQLiteOpenHelper {

        private static final int DataBaseVersion = 1;
        private static final String DataBaseName = "eventDatabase";
        private static final String Tablename = "eventTable";
        private static final String UID = "id";
        private static final String EventName = "eventname";
        private static final String EventDate = "eventdate";
        private static final String EventStarttime = "eventstarttime";
        private static final String EventEndTime = "eventendtime";
        private static final String EventClub = "eventclub";
        private static final String EventAttend = "eventattend";
        private static final String EventoganizernameFirst = "eventorganizerfirst";
        private static final String OrganizerMobFirst = "organizermobliefirst";
        private static final String OrganizerEmail = "organizeremail";
        private static final String EventVanue = "vanue";
        private static final String EventDescription = "eventdiscription";
        private static final String EventVarified = "verified";
        private static final String IsAdmin = "isadmin";
        private static final String EventoganizernameSecond = "eventoeganizernamesecond";
        private static final String OrganizerMobSecond = "eventmobnamesecond";
        private static final String EventServerID = "serverId";
        private static final String LastRegistrationTime = "registrationTime";
        private static final String EventLiked = "eventLiked";

        private static final String CREATETABLE = "CREATE TABLE " +
                Tablename + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventName + " VARCHAR(250), " + EventAttend + " VARCHAR(250), "
                + EventClub + " VARCHAR(250), " + EventDescription + " VARCHAR(250), "
                + EventVanue + " VARCHAR(250), " + EventVarified + " VARCHAR(250), "
                + EventoganizernameFirst + " VARCHAR(250), " + EventoganizernameSecond + " VARCHAR(250), "
                + OrganizerEmail + " VARCHAR(250), " + EventLiked + " VARCHAR(250), "
                + OrganizerMobFirst + " VARCHAR(250), " + OrganizerMobSecond + " VARCHAR(250), "
                + IsAdmin + " VARCHAR(250), " + EventServerID + " VARCHAR(250), "
                + LastRegistrationTime + " VARCHAR(250), " + EventStarttime + " VARCHAR(250), "
                + EventEndTime + " VARCHAR(250), " + EventDate + " VARCHAR(250));";

        private static final String DROPTABLE = "DROP TABLE IF EXISTS " + Tablename;

        public EventDataBaseHelper(Context context) {
            super(context, DataBaseName, null, DataBaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATETABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROPTABLE);
            onCreate(db);
        }
    }

}
