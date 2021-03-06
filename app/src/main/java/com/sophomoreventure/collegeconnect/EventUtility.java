package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.TypedValue;

import com.sophomoreventure.collegeconnect.extras.Constants;
import com.sophomoreventure.collegeconnect.extras.Constants.SharedPrefConstants;
import com.sophomoreventure.collegeconnect.GCM.RegistrationConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

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
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMMM,dd,yyyy hh:mm a");
        return shortenedDateFormat.format(dateInMillis);
    }

    public static boolean isEmpty(long[] array) {
        boolean empty = false;
        for (long anArray : array) {
            empty = anArray == 0;
        }
        return empty;
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
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(SharedPrefConstants.APP_SHARED_PREF_FIRST_RUN_KEY, true);
            editor.apply();
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

    public static boolean isUserVerified(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(SharedPrefConstants.USER_SHARED_PREF_USER_VERIFIED_KEY, false);

    }

    public static String getGCMToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefConstants.GCM_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(RegistrationConstants.TOKEN, null);

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

    public static String getUserTokenFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return user_pref.getString(SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, null);

    }

    public static String getUserEmailFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return user_pref.getString(SharedPrefConstants.USER_SHARED_PREF_EMAIL_KEY, null);

    }


    public static String getUserPasswordHashFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return user_pref.getString(SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, null);

    }

    public static String getUserRollNoFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return user_pref.getString(SharedPrefConstants.USER_SHARED_PREF_ROLL_NO_KEY, null);

    }

    public static void removeUserLoginFromPref(Context context) {
        SharedPreferences user_pref = context.getSharedPreferences(SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = user_pref.edit();
        editor.putBoolean(SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, false);
        editor.apply();

    }

    private static HashMap<String, String> getErrorHashMap() {
        HashMap<String, String> errorMap = new HashMap<>();
        //HTTP CODE 401 (UNAUTHORIZED ACCESS)
        errorMap.put("ERR01", "Login required");
        errorMap.put("ERR02", "Username empty");
        errorMap.put("ERR03", "Password empty");
        errorMap.put("ERR04", "Email is not registered");
        errorMap.put("ERR05", "Incorrect password");
        errorMap.put("ERR06", "Token Invalid");
        errorMap.put("ERR07", "Token expired");

        //HTTP CODE 405 (METHOD NOT ALLOWED)
        errorMap.put("ERR13", "Method not allowed");

        //HTTP CODE 409 (CONFLICT)
        errorMap.put("ERR14", "Username already taken");
        errorMap.put("ERR13", "Method not allowed");
        errorMap.put("ERR15", "Mobile number exists");
        errorMap.put("ERR16", "Email id already registered");
        errorMap.put("ERR17", "Email id and mobile number already registered");
        errorMap.put("ERR18", "Roll number already registered");
        errorMap.put("ERR19", "Roll number and mobile number already registered");
        errorMap.put("ERR20", "Roll number and email id already registered");
        errorMap.put("ERR21", "User with same info already registered");
        errorMap.put("ERR22", "Event with same already registered");
        return errorMap;
    }

    public static String getErrorString(String errorCode) {

        return getErrorHashMap().get(errorCode);
    }

    public static void clearUserSharedPref(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

    }
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 1;
            final int halfWidth = width / 1;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void markFirstRunDone(Context context, boolean b) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefConstants.APP_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SharedPrefConstants.APP_SHARED_PREF_FIRST_RUN_KEY, b);
        editor.apply();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private static HashMap<String, String> getColorHashMap() {
        HashMap<String, String> colorMap = new HashMap<>();
        colorMap.put("Blue", "#2196f3");
        colorMap.put("Purple", "#9c27b0");
        colorMap.put("Blue Grey", "#607d8b");
        colorMap.put("Teal", "#009688");

        return colorMap;
    }

    public static String getColorCode(String colorString) {

        return getColorHashMap().get(colorString);
    }

    private static String getJsonString(Context context,String fileName) throws FileNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");

            }
            br.close();
        } catch (IOException e) {
            Log.i("tag", "" + e);
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> getEventIdList(Context context) throws JSONException, FileNotFoundException {
        JSONArray jsonArray = new JSONArray(getJsonString(context,"events.txt"));
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(String.valueOf(jsonArray.getInt(i)));
            Log.i("vikas", "" + jsonArray.getInt(i));
        }
        return list;
    }

    public static ArrayList<String> getEventAttendingIdList(Context context) throws JSONException, FileNotFoundException {
        JSONArray jsonArray = new JSONArray(getJsonString(context,"attending.txt"));
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(String.valueOf(jsonArray.getInt(i)));
            Log.i("vikas", "" + jsonArray.getInt(i));
        }
        return list;
    }

}

