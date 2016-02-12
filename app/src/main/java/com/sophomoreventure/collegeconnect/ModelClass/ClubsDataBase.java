package com.sophomoreventure.collegeconnect.ModelClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sophomoreventure.collegeconnect.JsonHandler.ClubParserer;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 31-01-2016.
 */
public class ClubsDataBase {

    ClubDataBaseHelper helper;
    private Context context;

    public ClubsDataBase(Context context) {
        helper = new ClubDataBaseHelper(context);
        this.context = context;
    }

    public void insertRow(String clubId, String clubName,String clubDescription,String clubHeadName ,String mobno, String email
            ,String imageUrl) {

        if(isInDatabase(clubId)){
            deleteRow(clubId);
        }


        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClubDataBaseHelper.ClubID, clubId);
        contentValues.put(ClubDataBaseHelper.ClubName, clubName);
        contentValues.put(ClubDataBaseHelper.ClubDescription, clubDescription);
        contentValues.put(ClubDataBaseHelper.ClubHeadName, clubHeadName);
        contentValues.put(ClubDataBaseHelper.ClubHeadMob, mobno);
        contentValues.put(ClubDataBaseHelper.ClubHeadEmail, email);
        contentValues.put(ClubDataBaseHelper.ClubImageUrl,imageUrl);
        db.insert(ClubDataBaseHelper.Tablename, null, contentValues);
        Log.i("vikas","data inserted club");
    }


    public String getClubByID(String id){
        String nameq = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] whereArgs = {id};
        Cursor cursor = db.query(ClubDataBaseHelper.Tablename, null, ClubDataBaseHelper.ClubID + " =?", whereArgs,
                null, null, null, null);
        while(cursor.moveToNext()){
            nameq = cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubName));
        }

        return nameq;
    }

    public ClubModel viewAllData(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        ClubModel club = new ClubModel();
        String[] whereArgs = {name};
        Cursor cursor = db.query(ClubDataBaseHelper.Tablename, null, ClubDataBaseHelper.ClubName + " =?", whereArgs,
                null, null, null, null);
        while(cursor.moveToNext()){

            club.setClubName(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubName)));
            club.setClubDescription(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubDescription)));
            club.setClubHead(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubHeadName)));
            club.setClubHeadEmail(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubHeadEmail)));
            club.setClubHeadMob(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubHeadMob)));
            club.setImageUrl(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubImageUrl)));
            club.setClubID(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubID)));
        }
        return club;
    }


    public ArrayList<String> getClubTitles() {

        ArrayList<String> list = new ArrayList<>();
        String[] columns = {ClubDataBaseHelper.ClubName};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ClubDataBaseHelper.Tablename, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {

            list.add(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubName)));
        }
        cursor.close();
        return list;
    }


    public ArrayList<String> getClubTitlesNormal() {

        ArrayList<String> list = new ArrayList<>();
        String[] columns = {ClubDataBaseHelper.ClubName,ClubDataBaseHelper.ClubID};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ClubDataBaseHelper.Tablename, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubID)));

            if( id < 151) {
                list.add(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubName)));
            }

        }
        cursor.close();
        return list;
    }


    public ArrayList<String> getClubTitlesSparsh() {

        ArrayList<String> list = new ArrayList<>();
        String[] columns = {ClubDataBaseHelper.ClubName,ClubDataBaseHelper.ClubID};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(ClubDataBaseHelper.Tablename, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubID)));

            if( id > 150) {
                list.add(cursor.getString(cursor.getColumnIndex(ClubDataBaseHelper.ClubName)));
            }

        }
        cursor.close();
        return list;
    }


    public boolean isInDatabase(String clubId){
        String[] columns = {helper.ClubID};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor.getColumnIndex(helper.ClubID));

            if( id.equalsIgnoreCase(clubId)){
                return true;
            }
        }
        return false;
    }


    public int deleteRow(String clubID) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {clubID};
        int count = db.delete(helper.Tablename, helper.ClubID + " =?", whereArgs);
        return count;
    }


    public class ClubDataBaseHelper extends SQLiteOpenHelper {

        private static final int DataBaseVersion = 1;
        private static final String DataBaseName = "ClubDataBase";
        private static final String Tablename = "clubTable";
        private static final String UID = "id";
        private static final String ClubName = "clubName";
        private static final String ClubDescription = "clubDescription";
        private static final String ClubHeadName = "clubHeadName";
        private static final String ClubID = "clubId";
        private static final String ClubHeadMob = "mobno";
        private static final String ClubHeadEmail = "email";
        private static final String ClubImageUrl = "url";

        private static final String CREATETABLE = "CREATE TABLE " +
                Tablename + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ClubName + " VARCHAR(250), "
                + ClubDescription + " VARCHAR(250), " + ClubHeadName + " VARCHAR(250), "
                + ClubHeadMob + " VARCHAR(250), " + ClubHeadEmail + " VARCHAR(250), "
                + ClubImageUrl + " VARCHAR(250), "
                + ClubID + " VARCHAR(250));";

        private static final String DROPTABLE = "DROP TABLE IF EXISTS " + Tablename;

        public ClubDataBaseHelper(Context context) {
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
