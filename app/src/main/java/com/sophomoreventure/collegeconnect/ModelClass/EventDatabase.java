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

    public void insertData(List<EventModel> listData,PackageManager pm,boolean clearPrevious) {

        if (clearPrevious) {
            deleteDatabase();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < listData.size(); i++) {

            EventModel event = new EventModel();
            contentValues.put(helper.EventName, event.getEventName());
            contentValues.put(helper.EventDate, event.getEventDate());
            contentValues.put(helper.EventStarttime,event.getEventStarttime());
            contentValues.put(helper.EventEndTime, event.getEventEndTime());
            contentValues.put(helper.EventAttend,event.getEventAttend());
            contentValues.put(helper.EventClub,event.getEventClub());
            contentValues.put(helper.EventDescription,event.getEventDescription());
            contentValues.put(helper.EventLiked,event.getEventLiked());
            contentValues.put(helper.Eventoganizername,event.getEventoganizername());
            contentValues.put(helper.EventVanue,event.getEventVanue());
            contentValues.put(helper.OrganizerMob,event.getOrganizerMob());
            contentValues.put(helper.OrganizerEmail,event.getOrganizerEmail());
            contentValues.put(helper.EventVarified,event.getEventvarified());
            db.insert(helper.Tablename, null, contentValues);
        }

    }

    public void insertRow(String eventname,String eventdate,String eventstartdate,String eventenddate,
                          String eventattend,String eventclub,String eventdescription,String eventLiked
                            ,String eventorganizer,String eventVanue,String organiizermob,String organizeremail
                            ,String eventvarified){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.EventName, eventname);
        contentValues.put(helper.EventDate, eventdate);
        contentValues.put(helper.EventStarttime,eventstartdate);
        contentValues.put(helper.EventEndTime, eventenddate);
        contentValues.put(helper.EventAttend,eventattend);
        contentValues.put(helper.EventClub,eventclub);
        contentValues.put(helper.EventDescription,eventdescription);
        contentValues.put(helper.EventLiked,eventLiked);
        contentValues.put(helper.Eventoganizername,eventorganizer);
        contentValues.put(helper.EventVanue,eventVanue);
        contentValues.put(helper.OrganizerMob,organiizermob);
        contentValues.put(helper.OrganizerEmail,organizeremail);
        contentValues.put(helper.EventVarified,eventvarified);
        db.insert(helper.Tablename, null, contentValues);

    }

    public  void setAttendEvent(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.EventAttend, "true");
        String[] selecArgs = {"false"};
        db.update(helper.Tablename, contentValues, helper.EventAttend + " =?", selecArgs);
    }

    public ArrayList<EventModel> selectByClub(String club){
        ArrayList<EventModel> eventList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //String[] columns = {helper.UserEmail};
        String query = "SELECT * FROM "+ helper.Tablename +" WHERE "+
                helper.EventClub +"='"+ club +"';";
        String whereClause = helper.EventClub +"='"+ club +"';";
        String whereClause1 = helper.EventClub + " = ?";
        String[] whereArgs = {club};
        Cursor cursor = db.query(helper.Tablename, null, whereClause1, whereArgs, null, null, null);

        while (cursor.moveToNext()) {
            EventModel event = new EventModel();
            int index1 = cursor.getColumnIndex(helper.UID);
            int id = cursor.getInt(index1);
            event.setEventName(cursor.getString(cursor.getColumnIndex(helper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(helper.EventDescription)));
            event.setEventLiked(cursor.getString(cursor.getColumnIndex(helper.EventLiked)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(helper.EventAttend)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(helper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(helper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(helper.EventStarttime)));
            event.setEventDate(cursor.getString(cursor.getColumnIndex(helper.EventDate)));
            event.setEventVanue(cursor.getString(cursor.getColumnIndex(helper.EventVanue)));
            event.setEventoganizername(cursor.getString(cursor.getColumnIndex(helper.Eventoganizername)));
            event.setOrganizerEmail(cursor.getString(cursor.getColumnIndex(helper.OrganizerEmail)));
            event.setOrganizerMob(cursor.getString(cursor.getColumnIndex(helper.OrganizerMob)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(helper.EventVarified)));
            eventList.add(event);
        }
        return eventList;

    }

    public ArrayList<EventModel> viewAllData() {

        ArrayList<EventModel> eventList = new ArrayList<>();
        String[] columns = {helper.UID, helper.EventName, helper.EventDate,helper.EventStarttime,helper.EventEndTime,
        helper.EventAttend,helper.EventClub,helper.EventLiked,helper.EventDescription,helper.Eventoganizername,
        helper.EventVanue,helper.OrganizerMob,helper.OrganizerEmail,helper.EventVarified};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EventModel event = new EventModel();
            int index1 = cursor.getColumnIndex(helper.UID);
            int id = cursor.getInt(index1);
            event.setEventName(cursor.getString(cursor.getColumnIndex(helper.EventName)));
            event.setEventDescription(cursor.getString(cursor.getColumnIndex(helper.EventDescription)));
            event.setEventLiked(cursor.getString(cursor.getColumnIndex(helper.EventLiked)));
            event.setEventAttend(cursor.getString(cursor.getColumnIndex(helper.EventAttend)));
            event.setEventClub(cursor.getString(cursor.getColumnIndex(helper.EventClub)));
            event.setEventEndTime(cursor.getString(cursor.getColumnIndex(helper.EventEndTime)));
            event.setEventStarttime(cursor.getString(cursor.getColumnIndex(helper.EventStarttime)));
            event.setEventDate(cursor.getString(cursor.getColumnIndex(helper.EventDate)));
            event.setEventVanue(cursor.getString(cursor.getColumnIndex(helper.EventVanue)));
            event.setEventoganizername(cursor.getString(cursor.getColumnIndex(helper.Eventoganizername)));
            event.setOrganizerEmail(cursor.getString(cursor.getColumnIndex(helper.OrganizerEmail)));
            event.setOrganizerMob(cursor.getString(cursor.getColumnIndex(helper.OrganizerMob)));
            event.setEventvarified(cursor.getString(cursor.getColumnIndex(helper.EventVarified)));
            eventList.add(event);
        }

        return eventList;
    }

    public int deleteRow(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(helper.Tablename, helper.EventName + " =?", whereArgs);
        return count;
    }

    public void deleteDatabase(){
        //mDatabase.delete(UserDataBaseHelper.Tablename,null,null);
        context.deleteDatabase(helper.DataBaseName);

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
