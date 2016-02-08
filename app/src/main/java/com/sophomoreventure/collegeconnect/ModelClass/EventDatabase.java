package com.sophomoreventure.collegeconnect.ModelClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.Network.SqlDataListener;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 06-01-2016.
 */
public class EventDatabase {

    EventDataBaseHelper helper;
    private Context context;
    SqlLoaderEvent eventListLoader;
    SqlLoaderEventSelector eventSelector;

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
            contentValues.put(EventDataBaseHelper.EventColor, event.getEventTime());
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
            contentValues.put(EventDataBaseHelper.ClubId, event.getIsAdmin());
            contentValues.put(EventDataBaseHelper.OrganizerEmail, event.getOrganizerEmailOne());
            db.insert(EventDataBaseHelper.Tablename, null, contentValues);
        }

    }

    public void insertRow(String eventname, String eventColor, String eventstarttime,
                          String eventendtime, String eventattend, String eventclub,
                          String eventdescription, String eventfirstorganizer, String eventsecondorganizer,
                          String eventVanue, String organiizermobfirst, String organiizermobsecond, String organizeremail,
                          String eventvarified, String clubId, String serverID, String lastRegistrationTime,String eventCreatedBy
                          ,String url) {

        if(isInDatabase(serverID)){
            deleteRow(serverID);
        }


        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventName, eventname);
        contentValues.put(EventDataBaseHelper.EventColor, eventColor);
        contentValues.put(EventDataBaseHelper.EventStarttime, eventstarttime);
        contentValues.put(EventDataBaseHelper.EventEndTime, eventendtime);
        contentValues.put(EventDataBaseHelper.EventAttend, eventattend);
        contentValues.put(EventDataBaseHelper.EventClub, eventclub);
        contentValues.put(EventDataBaseHelper.EventCreatedBy,eventCreatedBy);
        contentValues.put(EventDataBaseHelper.EventDescription, eventdescription);
        contentValues.put(EventDataBaseHelper.EventoganizernameFirst, eventfirstorganizer);
        contentValues.put(EventDataBaseHelper.EventoganizernameSecond, eventsecondorganizer);
        contentValues.put(EventDataBaseHelper.EventVanue, eventVanue);
        contentValues.put(EventDataBaseHelper.OrganizerMobFirst, organiizermobfirst);
        contentValues.put(EventDataBaseHelper.OrganizerMobSecond, organiizermobsecond);
        contentValues.put(EventDataBaseHelper.OrganizerEmail, organizeremail);
        contentValues.put(EventDataBaseHelper.EventVarified, eventvarified);
        contentValues.put(EventDataBaseHelper.ClubId, clubId);
        contentValues.put(EventDataBaseHelper.EventServerID, serverID);
        contentValues.put(EventDataBaseHelper.ImageURL,url);
        contentValues.put(EventDataBaseHelper.LastRegistrationTime, lastRegistrationTime);
        db.insert(EventDataBaseHelper.Tablename, null, contentValues);

    }

    public void setAttendEvent(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventCreatedBy, "true");
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

    public void selectEventClubById(Context conn,String eventID){
        eventSelector = new SqlLoaderEventSelector(context,eventID);
        eventSelector.execute();
    }

    public void selectByClubName(Context con,String clubName){
        eventListLoader = new SqlLoaderEvent(context,clubName);
        eventListLoader.execute();
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
            event.setEventCreatedBy(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventCreatedBy)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventColor)));
            event.setEventVenue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventOrganizerOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameFirst)));
            event.setEventOrganizerTwo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameSecond)));
            event.setOrganizerEmailOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setEventOrganizerOnePhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobFirst)));
            event.setEventOrganizerTwoPhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobSecond)));
            event.setEventvarified((cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified))));
            event.setClubId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ClubId)));
            event.setEventServerId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventServerID)));
            event.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ImageURL)));
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
            event.setEventCreatedBy(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventCreatedBy)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventColor)));
            event.setEventVenue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventOrganizerOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameFirst)));
            event.setEventOrganizerTwo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameSecond)));
            event.setOrganizerEmailOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setEventOrganizerOnePhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobFirst)));
            event.setEventOrganizerTwoPhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobSecond)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified)));
            event.setClubId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ClubId)));
            event.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ImageURL)));
            event.setEventServerId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventServerID)));
            event.setLastRegistrationTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.LastRegistrationTime)));
            eventList.add(event);
        }
        cursor.close();
        return eventList;
    }

    public ArrayList<Event> viewAllData() {

        ArrayList<Event> eventList = new ArrayList<>();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(EventDataBaseHelper.Tablename, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Event event = new Event();

            event.setEventTitle(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDescription)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventAttend)));
            event.setEventCreatedBy(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventCreatedBy)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventColor)));
            event.setEventVenue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventOrganizerOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameFirst)));
            event.setEventOrganizerTwo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventoganizernameSecond)));
            event.setOrganizerEmailOne(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setEventOrganizerOnePhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobFirst)));
            event.setEventOrganizerTwoPhoneNo(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMobSecond)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified)));
            event.setClubId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ClubId)));
            event.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ImageURL)));
            event.setEventServerId(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventServerID)));
            event.setLastRegistrationTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.LastRegistrationTime)));
            eventList.add(event);
        }
        cursor.close();
        Log.i("vikas", eventList.size() + " all");
        return eventList;
    }

    public ArrayList<Event> viewSlideShowData() {
        ArrayList<Event> eventList = new ArrayList<>();
        String[] columns = {helper.ImageURL};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(EventDataBaseHelper.Tablename,columns , null, null, null, null, null);
        while(cursor.moveToNext()){
            Event event = new Event();
            event.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.ImageURL)));
            eventList.add(event);
        }
        return eventList;
    }



    public boolean isInDatabase(String serverId){
        String[] columns = {helper.EventServerID};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(helper.EventServerID));

            if( id.equalsIgnoreCase(serverId)){
                return true;
            }
        }
        return false;
    }


    public int deleteRow(String serverID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {serverID};
        int count = db.delete(EventDataBaseHelper.Tablename, EventDataBaseHelper.EventServerID + " =?", whereArgs);
        return count;
    }

    public void deleteDatabase() {
        //mDatabase.delete(UserDataBaseHelper.Tablename,null,null);
        context.deleteDatabase(EventDataBaseHelper.DataBaseName);

    }

    public class EventDataBaseHelper extends SQLiteOpenHelper {

        private static final int DataBaseVersion = 2;
        private static final String DataBaseName = "eventDatabase";
        private static final String Tablename = "eventTable";
        private static final String UID = "id";
        private static final String EventName = "eventname";
        private static final String EventColor = "eventdate";
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
        private static final String ClubId = "isadmin";
        private static final String EventoganizernameSecond = "eventoeganizernamesecond";
        private static final String OrganizerMobSecond = "eventmobnamesecond";
        private static final String EventServerID = "serverId";
        private static final String LastRegistrationTime = "registrationTime";
        private static final String EventCreatedBy = "eventLiked";
        private static final String ImageURL = "imageurl";

        private static final String CREATETABLE = "CREATE TABLE " +
                Tablename + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventName + " VARCHAR(250), " + EventAttend + " VARCHAR(250), "
                + EventClub + " VARCHAR(250), " + EventDescription + " VARCHAR(250), "
                + EventVanue + " VARCHAR(250), " + EventVarified + " VARCHAR(250), "
                + EventoganizernameFirst + " VARCHAR(250), " + EventoganizernameSecond + " VARCHAR(250), "
                + OrganizerEmail + " VARCHAR(250), " + EventCreatedBy + " VARCHAR(250), "
                + OrganizerMobFirst + " VARCHAR(250), " + OrganizerMobSecond + " VARCHAR(250), "
                + ClubId + " VARCHAR(250), " + EventServerID + " VARCHAR(250), "
                + ImageURL + " VARCHAR(250), "
                + LastRegistrationTime + " VARCHAR(250), " + EventStarttime + " VARCHAR(250), "
                + EventEndTime + " VARCHAR(250), " + EventColor + " VARCHAR(250));";

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

    public class SqlLoaderEvent extends AsyncTask<Void, Void, ArrayList<Event>> {

        private Context context;
        private String clubName;
        SqlDataListener listener;

        public SqlLoaderEvent(Context context,String clubName){
            this.context = context;
            this.clubName = clubName;
            listener = (SqlDataListener) context;
        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {

            if(isCancelled()){
                return null;
            }
            ArrayList<Event> list= selectByClub(clubName);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Event> listEvents) {
            if(listener != null){
                listener.loadData(listEvents);
            }
            cancel(true);

        }
    }


    public class SqlLoaderEventSelector extends AsyncTask<Void, Void,Event> {

        private Context context;
        private String clubId;
        SqlDataListener listener;

        public SqlLoaderEventSelector(Context context,String clubId){
            this.context = context;
            this.clubId = clubId;
            listener = (SqlDataListener) context;
        }

        @Override
        protected Event doInBackground(Void... params) {

            if(isCancelled()){
                return null;
            }
            Event list= selectByEventId(clubId);
            return list;
        }

        @Override
        protected void onPostExecute(Event event) {
            if(listener != null){
                listener.loadEventById(event);
            }

            cancel(true);
        }
    }

}
