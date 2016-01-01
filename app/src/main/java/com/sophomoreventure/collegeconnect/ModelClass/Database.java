package com.sophomoreventure.collegeconnect.ModelClass;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vikas Kumar on 21-12-2015.
 */
public class Database {

    DataBaseHelper helper;
    private SQLiteDatabase mDatabase;
    private Context context;

    public Database(Context context) {
        helper = new DataBaseHelper(context);
        this.context = context;
    }

    public void insertData(List<ApplicationInfo> listData,PackageManager pm,boolean clearPrevious) {

        if (clearPrevious) {
            deleteDatabase();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < listData.size(); i++) {

            ApplicationInfo current = listData.get(i);
            contentValues.put(helper.Name, (String) current.loadLabel(pm));
            contentValues.put(helper.UserPassword, current.packageName);
            // db.insert(helper.Tablename, null, contentValues);

        }

    }

    public void insertRow(String pkg,String appName){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.Name, appName);
        contentValues.put(helper.UserPassword, pkg);
        db.insert(helper.Tablename, null, contentValues);

    }

    public ArrayList<AppInfo> viewAllData() {

        ArrayList<AppInfo> list = new ArrayList<>();
        String[] columns = {helper.UID, helper.Name, helper.UserPassword};
        StringBuffer buffer = new StringBuffer();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            AppInfo app = new AppInfo();
            int index1 = cursor.getColumnIndex(helper.UID);
            int id = cursor.getInt(index1);
            app.setAppName(cursor.getString(cursor.getColumnIndex(helper.Name)));
            app.setPkgName(cursor.getString(cursor.getColumnIndex(helper.UserPassword)));
            list.add(app);
        }

        return list;
    }

    public ArrayList<Integer> viewIdData() {

        ArrayList<Integer> idList = new ArrayList<>();
        String[] columns = {helper.UID};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index1 = cursor.getColumnIndex(helper.UID);
            int id = cursor.getInt(index1);
            idList.add(id);
        }

        return idList;
    }

    public ArrayList<String> viewNameData() {

        ArrayList<String> nameList = new ArrayList<String>();
        String[] columns = {helper.Name};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index2 = cursor.getColumnIndex(helper.Name);
            String name = cursor.getString(index2);
            nameList.add(name);
        }

        return nameList;
    }

    public ArrayList<String> viewPjgNameData() {

        ArrayList<String> passwordList = new ArrayList<String>();
        String[] columns = {helper.UserPassword};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index3 = cursor.getColumnIndex(helper.UserPassword);
            String password = cursor.getString(index3);
            passwordList.add(password);
        }

        return passwordList;
    }

    public boolean isInDatabase(String pkg,String appName){
        String[] columns = {helper.UserPassword,helper.Name};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(helper.Name));
            String pkgName = cursor.getString(cursor.getColumnIndex(helper.UserPassword));

            if(name.equalsIgnoreCase(appName) && pkgName.equalsIgnoreCase(pkg)){
                return true;
            }
        }
        return false;
    }

    public boolean addIfNotinDatabase(String pkg,String appName){
        adapterLockedApp = new AdapterLockedApp(context);
        AppInfo app = new AppInfo();
        app.setPkgName(pkg);
        app.setAppName(appName);
        if(isInDatabase(pkg,appName)){
            deleteRow(appName);
            //adapterLockedApp.removeItem(app);
            return false;
        }else{
            insertRow(pkg,appName);
            //adapterLockedApp.addItem(app);
            return true;
        }
    }

    public void updateName(String oldName, String newName) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.Name, newName);
        String[] selecArgs = {oldName};
        db.update(helper.Tablename, contentValues, helper.Name + " =?", selecArgs);

    }

    public int deleteRow(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(helper.Tablename, helper.Name + " =?", whereArgs);
        return count;
    }

    public void deleteDatabase(){
        //mDatabase.delete(DataBaseHelper.Tablename,null,null);
        context.deleteDatabase(helper.DataBaseName);

    }

    public class DataBaseHelper extends SQLiteOpenHelper {

        private static final int DataBaseVersion = 1;
        private static final String DataBaseName = "userDatabase";
        private static final String Tablename = "usrTable";
        private static final String UID = "id";
        private static final String Name = "name";
        private static final String UserPassword = "userPassword";
        private static final String UserToken = "userToken";
        private static final String UserEmail = "email";
        private static final String CREATETABLE = "CREATE TABLE " +
                Tablename + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Name + " VARCHAR(250), " + UserToken + " VARCHAR(250), "+ UserEmail + " VARCHAR(250), " + UserPassword + " VARCHAR(250));";

        private static final String DROPTABLE = "DROP TABLE IF EXISTS" + Tablename;

        public DataBaseHelper(Context context) {
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
