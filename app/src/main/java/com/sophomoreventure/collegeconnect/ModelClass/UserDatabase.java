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
 * Created by Vikas Kumar on 21-12-2015.
 */
public class UserDatabase {

    UserDataBaseHelper helper;
    private SQLiteDatabase mDatabase;
    private Context context;

    public UserDatabase(Context context) {
        helper = new UserDataBaseHelper(context);
        this.context = context;
    }

    public void insertData(List<User> listData,PackageManager pm,boolean clearPrevious) {

        if (clearPrevious) {
            deleteDatabase();
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i = 0; i < listData.size(); i++) {

            User user = new User();
            contentValues.put(helper.Name, user.getUserName());
            contentValues.put(helper.UserPassword, user.getPassword());
            contentValues.put(helper.UserToken,user.getToken());
            contentValues.put(helper.UserEmail,user.getEmail());
            db.insert(helper.Tablename, null, contentValues);
        }

    }

    public void insertRow(String name,String password,String token,String email){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.Name, name);
        contentValues.put(helper.UserPassword, password);
        contentValues.put(helper.UserToken,token);
        contentValues.put(helper.UserEmail,email);
        db.insert(helper.Tablename, null, contentValues);

    }

    public ArrayList<User> viewAllData() {

        ArrayList<User> list = new ArrayList<>();
        String[] columns = {helper.UID, helper.Name, helper.UserPassword,helper.UserToken,helper.UserEmail};
        StringBuffer buffer = new StringBuffer();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            User user = new User();
            int index1 = cursor.getColumnIndex(helper.UID);
            int id = cursor.getInt(index1);
            user.setUserName(cursor.getString(cursor.getColumnIndex(helper.Name)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(helper.UserPassword)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(helper.UserEmail)));
            user.setToken(cursor.getString(cursor.getColumnIndex(helper.UserToken)));
            list.add(user);
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

    public ArrayList<String> viewUserPasswordData() {

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

    public ArrayList<String> viewUserEmailData() {

        ArrayList<String> emailList = new ArrayList<String>();
        String[] columns = {helper.UserEmail};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index3 = cursor.getColumnIndex(helper.UserEmail);
            String email = cursor.getString(index3);
            emailList.add(email);
        }

        return emailList;
    }

    public ArrayList<String> viewUserTokenData() {

        ArrayList<String> tokenData = new ArrayList<String>();
        String[] columns = {helper.UserToken};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            int index3 = cursor.getColumnIndex(helper.UserToken);
            String token = cursor.getString(index3);
            tokenData.add(token);
        }

        return tokenData;
    }

    public boolean isInDatabase(String userName,String userEmail){
        String[] columns = {helper.UserEmail,helper.Name};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(helper.Name));
            String email = cursor.getString(cursor.getColumnIndex(helper.UserEmail));

            if(name.equalsIgnoreCase(userName) && email.equalsIgnoreCase(userEmail)){
                return true;
            }
        }
        return false;
    }

    public boolean addIfNotinDatabase(String name,String password,String token,String email){
        User user = new User();
        user.setUserName(name);
        user.setToken(token);
        user.setEmail(email);
        user.setPassword(password);

        if(isInDatabase(name,email)){

            return false;
        }else{
            insertRow(name,password,token,email);
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

    public boolean updatePassword(String usrName,String oldPassword, String newPassword) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.UserPassword, newPassword);
        String[] selecArgs = {oldPassword};
        if(isInDatabase(usrName,oldPassword)){
            db.update(helper.Tablename, contentValues, helper.UserPassword + " =?", selecArgs);
            return true;
        }else {
            return false;
        }
    }

    public boolean updateToken(String Name,String token){

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {helper.UserToken};
        Cursor cursor = db.query(helper.Tablename, columns, null, null, null, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(helper.UserToken, token);
        String tokendata = "";
        while (cursor.moveToNext()) {

            int index3 = cursor.getColumnIndex(helper.UserToken);
            tokendata = cursor.getString(index3);

        }
        String[] selecArgs = {tokendata};
        db.update(helper.Tablename, contentValues, helper.UserToken + " =?", selecArgs);
        return true;
    }

    public int deleteRow(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(helper.Tablename, helper.Name + " =?", whereArgs);
        return count;
    }

    public void deleteDatabase(){
        //mDatabase.delete(UserDataBaseHelper.Tablename,null,null);
        context.deleteDatabase(helper.DataBaseName);

    }

    public class UserDataBaseHelper extends SQLiteOpenHelper {

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

        public UserDataBaseHelper(Context context) {
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
