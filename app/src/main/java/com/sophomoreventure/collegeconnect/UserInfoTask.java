package com.sophomoreventure.collegeconnect;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.sophomoreventure.collegeconnect.extras.API;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Murali on 30/12/2015.
 */
public class UserInfoTask extends AsyncTask<Void, Void, Void> {
    JSONObject jsonObject = new JSONObject();

    @Override
    protected Void doInBackground(Void... voids) {

        String urlString = API.USER_PROFILE_API;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);


            String userCredentials = "vikas:password";
            final String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            byte[] outputBytes = createJson().toString().getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            connection.connect();

            int responseCode = connection.getResponseCode();
            //String responseMsg = connection.getResponseMessage();
            Log.i("tag", "RESPONSE CODE: " + responseCode);



        } catch (Exception e) {
            Log.d("tag", e.getLocalizedMessage());
        }
        return null;

    }

    JSONObject createJson() throws JSONException {
        jsonObject.put("name", "vikas");
        jsonObject.put("rollno", "u13ec034");
        jsonObject.put("email", "bc@gmail.com");
        jsonObject.put("mobno", 997850665);
        Log.i("tag", jsonObject.toString());
        return jsonObject;
    }
}

