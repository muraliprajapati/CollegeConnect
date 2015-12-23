package com.sophomoreventure.collegeconnect.ModelClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 21-12-2015.
 */
public class Database {

    public static final int COLLEGE_CONNECT = 0;
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    public Database(Context context) {
        mHelper = new DatabaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertData(int table, ArrayList<String> listData, boolean clearPrevious) {
        if (clearPrevious) {
            deleteDatabase(table);
        }

        //create a sql prepared statement
        String sql = "INSERT INTO " + (table == COLLEGE_CONNECT ? DatabaseHelper.TABLE_COLLEGE_CONNECT : DatabaseHelper.TABLE_UPCOMING) + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listData.size(); i++) {


            //String current = listData.get(i);
            //statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            //statement.bindString(2, current.getTitle());


            statement.execute();
        }

        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<String> readData(int table) {
        ArrayList<String> listData = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {DatabaseHelper.COLUMN_UID,
                DatabaseHelper.COLUMN_TITLE,
        };
        Cursor cursor = mDatabase.query((table == COLLEGE_CONNECT ? DatabaseHelper.TABLE_COLLEGE_CONNECT : DatabaseHelper.TABLE_UPCOMING), columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            do {

                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                //Data data = new Data();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank movie object to contain our data
                //data.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)));

                //add the movie to the list of movie objects which we plan to return
                //listData.add(data);
            }
            while (cursor.moveToNext());
        }
        return listData;
    }

    public void deleteDatabase(int table) {
        mDatabase.delete((table == COLLEGE_CONNECT ? DatabaseHelper.TABLE_COLLEGE_CONNECT : DatabaseHelper.TABLE_UPCOMING), null, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public static final String TABLE_UPCOMING = " upcoming";
        public static final String TABLE_COLLEGE_CONNECT = "movies_box_office";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_TITLE = "title";

        private static final String CREATE_TABLE_COLLEGE_CONNECT = "CREATE TABLE " + TABLE_COLLEGE_CONNECT + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                ");";

        private static final String CREATE_TABLE_UPCOMING = "CREATE TABLE " + TABLE_UPCOMING + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                ");";

        private static final String DB_NAME = "events_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_COLLEGE_CONNECT);
                db.execSQL(CREATE_TABLE_UPCOMING);

            } catch (SQLiteException exception) {

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL(" DROP TABLE " + TABLE_COLLEGE_CONNECT + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_UPCOMING + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {

            }
        }
    }

}
