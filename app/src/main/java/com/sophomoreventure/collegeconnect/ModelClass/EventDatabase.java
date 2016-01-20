package com.sophomoreventure.collegeconnect.ModelClass;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikas Kumar on 06-01-2016.
 */
public class EventDatabase {

    EventDataBaseHelper helper;
    private SQLiteDatabase mDatabase;
    private Context context;

    public EventDatabase(Context context) {
        helper = new EventDataBaseHelper(context);
        this.context = context;
    }

    public void insertData(List<EventModel> listData, PackageManager pm, boolean clearPrevious) {

        if (clearPrevious) {
            deleteDatabase();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < listData.size(); i++) {

            EventModel event = new EventModel();
            contentValues.put(EventDataBaseHelper.EventName, event.getEventName());
            contentValues.put(EventDataBaseHelper.EventDate, event.getEventDate());
            contentValues.put(EventDataBaseHelper.EventStarttime, event.getEventStarttime());
            contentValues.put(EventDataBaseHelper.EventEndTime, event.getEventEndTime());
            contentValues.put(EventDataBaseHelper.EventAttend, event.getEventAttend());
            contentValues.put(EventDataBaseHelper.EventClub, event.getEventClub());
            contentValues.put(EventDataBaseHelper.EventDescription, event.getEventDescription());
            contentValues.put(EventDataBaseHelper.EventLiked, event.getEventLiked());
            contentValues.put(EventDataBaseHelper.Eventoganizername, event.getEventoganizername());
            contentValues.put(EventDataBaseHelper.EventVanue, event.getEventVanue());
            contentValues.put(EventDataBaseHelper.OrganizerMob, event.getOrganizerMob());
            contentValues.put(EventDataBaseHelper.OrganizerEmail, event.getOrganizerEmail());
            contentValues.put(EventDataBaseHelper.EventVarified, event.getEventvarified());
            db.insert(EventDataBaseHelper.Tablename, null, contentValues);
        }

    }

    public void insertRow(String eventname, String eventdate, String eventstartdate,
                          String eventenddate, String eventattend, String eventclub,
                          String eventdescription, String eventLiked, String eventorganizer,
                          String eventVanue, String organiizermob, String organizeremail,
                          String eventvarified) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventName, eventname);
        contentValues.put(EventDataBaseHelper.EventDate, eventdate);
        contentValues.put(EventDataBaseHelper.EventStarttime, eventstartdate);
        contentValues.put(EventDataBaseHelper.EventEndTime, eventenddate);
        contentValues.put(EventDataBaseHelper.EventAttend, eventattend);
        contentValues.put(EventDataBaseHelper.EventClub, eventclub);
        contentValues.put(EventDataBaseHelper.EventDescription, eventdescription);
        contentValues.put(EventDataBaseHelper.EventLiked, eventLiked);
        contentValues.put(EventDataBaseHelper.Eventoganizername, eventorganizer);
        contentValues.put(EventDataBaseHelper.EventVanue, eventVanue);
        contentValues.put(EventDataBaseHelper.OrganizerMob, organiizermob);
        contentValues.put(EventDataBaseHelper.OrganizerEmail, organizeremail);
        contentValues.put(EventDataBaseHelper.EventVarified, eventvarified);
        db.insert(EventDataBaseHelper.Tablename, null, contentValues);

    }

    public void setAttendEvent() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventDataBaseHelper.EventAttend, "true");
        String[] selecArgs = {"false"};
        db.update(EventDataBaseHelper.Tablename, contentValues,
                EventDataBaseHelper.EventAttend + " =?", selecArgs);
    }

    public ArrayList<EventModel> selectByClub(String club) {
        ArrayList<EventModel> eventList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //String[] columns = {helper.UserEmail};
        String query = "SELECT * FROM " + EventDataBaseHelper.Tablename + " WHERE " +
                EventDataBaseHelper.EventClub + "='" + club + "';";
        String whereClause = EventDataBaseHelper.EventClub + "='" + club + "';";
        String whereClause1 = EventDataBaseHelper.EventClub + " = ?";
        String[] whereArgs = {club};
        Cursor cursor = db.query(EventDataBaseHelper.Tablename, null, whereClause1, whereArgs,
                null, null, null);


        while (cursor.moveToNext()) {
            EventModel event = new EventModel();
            int index1 = cursor.getColumnIndex(EventDataBaseHelper.UID);
            int id = cursor.getInt(index1);
            event.setEventName(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDescription)));
            event.setEventLiked(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventLiked)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventAttend)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventDate(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDate)));
            event.setEventVanue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventoganizername(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.Eventoganizername)));
            event.setOrganizerEmail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setOrganizerMob(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMob)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified)));
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
                EventDataBaseHelper.EventClub, EventDataBaseHelper.EventLiked,
                EventDataBaseHelper.EventDescription, EventDataBaseHelper.Eventoganizername,
                EventDataBaseHelper.EventVanue, EventDataBaseHelper.OrganizerMob,
                EventDataBaseHelper.OrganizerEmail, EventDataBaseHelper.EventVarified};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(EventDataBaseHelper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EventModel event = new EventModel();
            int index1 = cursor.getColumnIndex(EventDataBaseHelper.UID);
            int id = cursor.getInt(index1);
            event.setEventName(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDescription)));
            event.setEventLiked(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventLiked)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventAttend)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventStarttime)));
            event.setEventDate(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventDate)));
            event.setEventVanue(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVanue)));
            event.setEventoganizername(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.Eventoganizername)));
            event.setOrganizerEmail(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerEmail)));
            event.setOrganizerMob(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.OrganizerMob)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(EventDataBaseHelper.EventVarified)));
            eventList.add(event);
        }
        cursor.close();

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
        private static final String EventLiked = "eventliked";
        private static final String EventClub = "eventclub";
        private static final String EventAttend = "eventattend";
        private static final String Eventoganizername = "eventorganizer";
        private static final String OrganizerMob = "organizermoblie";
        private static final String OrganizerEmail = "organizeremail";
        private static final String EventVanue = "vanue";
        private static final String EventDescription = "eventdiscription";
        private static final String EventVarified = "verified";

        private static final String CREATETABLE = "CREATE TABLE " +
                Tablename + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventName + " VARCHAR(250), " + EventLiked + " VARCHAR(250), "
                + EventClub + " VARCHAR(250), " + EventDescription + " VARCHAR(250), "
                + EventVanue + " VARCHAR(250), " + EventVarified + " VARCHAR(250), "
                + OrganizerMob + " VARCHAR(250), "
                + OrganizerEmail + " VARCHAR(250), " + EventAttend + " VARCHAR(250), "
                + Eventoganizername + " VARCHAR(250), " + EventStarttime + " VARCHAR(250), "
                + EventEndTime + " VARCHAR(250), " + EventDate + " VARCHAR(250));";

        private static final String DROPTABLE = "DROP TABLE IF EXISTS" + Tablename;

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
