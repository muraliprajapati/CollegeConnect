package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.util.Log;

import com.sophomoreventure.collegeconnect.Activities.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vikas Kumar on 01-01-2016.
 */
public class Parserer {

    public static boolean parseResponse(JSONObject jsonObject,Context context)  {
        String response = null;
        DataListener dataListener;
        try {
            dataListener = (DataListener) context;
            response = jsonObject.getString("Status");
            Log.i("vikas",response);
            if(response.equalsIgnoreCase("Username not unique")){
                dataListener.onDataLoaded(true);
                return true;
            }else {
                dataListener.onDataLoaded(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
