package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vikas Kumar on 01-01-2016.
 */
public class Parserer {

    public static boolean parseResponse(JSONObject jsonObject, Context context) {
        String response = null;
        DataListener dataListener;
        try {
            dataListener = (DataListener) context;
            response = jsonObject.getString("message");
            Log.i("vikas", response);
            if (response.equalsIgnoreCase("ERR14")) {
                dataListener.onDataLoaded(true);
                return true;
            } else {
                dataListener.onDataLoaded(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String parseErrorResponse(JSONObject jsonObject) {
        String response = null;
        try {

            response = jsonObject.getString("message");
            Log.i("vikas", response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String parseToken(JSONObject jsonObject) {
        String response = null;
        try {

            response = jsonObject.getString("token");
            Log.i("vikas", response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}
