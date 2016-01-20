package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.SharedPreferences;

import com.sophomoreventure.collegeconnect.Constants.SharedPrefConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Murali on 24/12/2015.
 */
public class EventUtility {
    public static String getFriendlyDayString(long dateInMillis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateInMillis);
        if (calendar.equals(new GregorianCalendar(1970, 0, 1))) {
            return "Notification";
        }
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMMM,dd,yyyy HH:mm a");
        return shortenedDateFormat.format(dateInMillis);
    }

    public static boolean isEmpty(long[] array) {
        boolean empty = false;
        for (long anArray : array) {
            empty = anArray == 0;
        }
        return empty;
    }

    public static JSONObject getJsonForRegistration(String rollNo, String name, String email, int mobileNo) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rollno", rollNo);
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("mobno", mobileNo);
        return jsonObject;

    }

    public static boolean isFirstRun(Context context) {

        final int DOESNT_EXIST = -1;
        boolean firstRun = false;


        // Get current version code
        int currentVersionCode = 0;
        currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefConstants.APP_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(SharedPrefConstants.APP_SHARED_PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            firstRun = false;

        } else if (savedVersionCode == DOESNT_EXIST) {

            firstRun = true;

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
            firstRun = true;

        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(SharedPrefConstants.APP_SHARED_PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        return firstRun;


    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, false);
    }

    public static String getHashString(String message, String algorithm) {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte arrayByte : arrayBytes) {
            stringBuilder.append(Integer.toString((arrayByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuilder.toString();
    }

    public static String getUserNameFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return user_pref.getString(SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, null);

    }


    public static String getUserPasswordHashFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return user_pref.getString(SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, null);

    }
}

