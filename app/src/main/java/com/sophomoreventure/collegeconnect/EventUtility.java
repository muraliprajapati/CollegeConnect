package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

import com.sophomoreventure.collegeconnect.Constants.SharedPrefConstants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
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
}

