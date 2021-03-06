package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.HttpsTrustManager;
import com.sophomoreventure.collegeconnect.JsonHandler.ClubParserer;
import com.sophomoreventure.collegeconnect.ParserEventResponse;
import com.sophomoreventure.collegeconnect.extras.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class RequestorGet {

    private static JSONObject jsonObject;

    public static JSONObject requestDataJSON(RequestQueue requestQueue, String url) {
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, (String) null, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static JSONObject requestJsonData(
            final RequestQueue requestQueue, String url, final String userName,
            final String userPassword, final Context context) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());

                        Parserer.parseResponse(response, context);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");
                        NetworkResponse response = error.networkResponse;
                        Log.i("vikas", response.statusCode + "");
                        String string = new String(response.data);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            Log.i("vikas", response.statusCode + ":" + jsonObject.toString());
                            Parserer.parseResponse(jsonObject, context);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", userName, userPassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        requestQueue.add(request);
        return jsonObject;

    }

    public static  void requestEventData(
            final RequestQueue requestQueue, String url, final Context context) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("vikas", response.toString());
                        ArrayList<Event> listEvents = ParserEventResponse.parseEventsJSON(response,context);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");
                    }
                }) {


        };
        requestQueue.add(request);

    }




    public static void requestLogin(
            final RequestQueue requestQueue, final String url, final String userEmail,
            final String userPassword, final Context context) {
        Log.i("vikas", "in Login Request");
        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        String token = Parserer.parseToken(response);
                        try {
                            parseAndSaveToPref(context, userEmail, userPassword, token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listener.onDataLoaded(url);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");

                        DataListener listener = (DataListener) context;
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            listener.setError(url, "NOCON");

                        }else {
                            try {
                                NetworkResponse response = error.networkResponse;
                                Log.i("vikas", response.statusCode + "");
                                String string = new String(response.data);
                                JSONObject jsonObject = new JSONObject(string);
                                Log.i("vikas", response.statusCode + ":" + jsonObject.toString());
                                listener.setError(url, Parserer.parseResponse(jsonObject));

                            } catch (JSONException e) {
                                Log.i("vikas", "" + e);
                                e.printStackTrace();
                            }
                        }


                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", userEmail, userPassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        requestQueue.add(request);
    }

    public static void requestUserInfo(
            final RequestQueue requestQueue, final String url, final String userEmail,
            final String userPassword, final Context context) {
        Log.i("vikas", "in user info Request");

        HttpsTrustManager.allowAllSSL();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        try {
                            parseAndSaveUserInfoToPref(context, response);
                            parseAndSaveClubAdmin(context, response);
                            parseAndSaveEvents(context, response);
                            parseAndSaveEventFollowed(context,response);
                            listener.onDataLoaded(url);
                        } catch (JSONException e) {
                            Log.i("vikas", "" + e);
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");

                        DataListener listener = (DataListener) context;
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            listener.setError(url, "NOCON");

                        } else {
                            try {
                                NetworkResponse response = error.networkResponse;
                                Log.i("vikas", response.statusCode + "");
                                String string = new String(response.data);
                                JSONObject jsonObject = new JSONObject(string);
                                Log.i("vikas", response.statusCode + ":" + jsonObject.toString());
                                listener.setError(url, Parserer.parseResponse(jsonObject));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", userEmail, userPassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        requestQueue.add(request);
    }

    private static void parseAndSaveEvents(Context context, JSONObject response) throws JSONException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray eventIdArray = response.getJSONArray("my_events");

//        for (int i = 0; i < eventIdArray.length(); i++) {
//            int eventID = eventIdArray.getInt(i);
//            String eventsIDString = String.valueOf(eventID) + "\n";
//            stringBuilder.append(eventsIDString);
//        }
        FileOutputStream fileout = context.openFileOutput("events.txt", Context.MODE_PRIVATE);
        OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
        outputWriter.write(eventIdArray.toString());
        outputWriter.close();
    }

    private static void parseAndSaveClubAdmin(Context context, JSONObject response) throws JSONException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray clubAdminArray = response.getJSONArray("club_admin");
//        for (int i = 0; i < clubAdminArray.length(); i++) {
//            int clubID = clubAdminArray.getInt(i);
//            String clubIDString = String.valueOf(clubID) + "\n";
//            stringBuilder.append(clubIDString);
//        }
        FileOutputStream fileout = context.openFileOutput("clubs.txt", Context.MODE_PRIVATE);
        OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
        outputWriter.write(clubAdminArray.toString());
        outputWriter.close();
    }

    private static void parseAndSaveEventFollowed(Context context, JSONObject response) throws JSONException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray clubAdminArray = response.getJSONArray("events_attending");
//        for (int i = 0; i < clubAdminArray.length(); i++) {
//            int clubID = clubAdminArray.getInt(i);
//            String clubIDString = String.valueOf(clubID) + "\n";
//            stringBuilder.append(clubIDString);
//        }
        FileOutputStream fileout = context.openFileOutput("attending.txt", Context.MODE_PRIVATE);
        OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
        outputWriter.write(clubAdminArray.toString());
        outputWriter.close();
    }

    private static void parseAndSaveToPref(Context context, String userEmail, String userPassword,
                                           String token) throws JSONException {
        Log.i("vikas", "parseAndSaveToPref");

        SharedPreferences prefs = context.getSharedPreferences(
                Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, true);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_EMAIL_KEY, userEmail);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, userPassword);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, token);
        editor.commit();

        Log.i("vikas", token);
        Log.i("vikas", EventUtility.getHashString(userPassword, "SHA-1"));

    }

    private static void parseAndSaveUserInfoToPref(Context context, JSONObject response) throws JSONException {
        Log.i("vikas", "parseAndSaveUserInfoToPref" + response.toString());
        String email = response.getString("email");
        String name = response.getString("name");
        String mobNo = response.getString("mobno");
        String rollNo = response.getString("rollno");
        String varified = response.getString("verified");
        boolean verified = varified.equalsIgnoreCase("true");

        Log.i("vikas", "user info:" + email);
        Log.i("vikas", "user info:" + name);
        Log.i("vikas", "user info:" + rollNo);
        Log.i("vikas", "user info:" + mobNo);

        SharedPreferences prefs = context.getSharedPreferences(
                Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_EMAIL_KEY, email);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, name);
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_VERIFIED_KEY, verified);
        if (!mobNo.equalsIgnoreCase("null") && !mobNo.isEmpty()) {

            editor.putLong(Constants.SharedPrefConstants.USER_SHARED_PREF_MOB_NO_KEY, Long.parseLong(mobNo));
        }

        if (!rollNo.equalsIgnoreCase("null") && !rollNo.isEmpty()) {
            rollNo = response.getString("rollno");
            editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_ROLL_NO_KEY, rollNo);
        }
        editor.commit();
        Log.i("vikas", "parseAndSaveUserInfoToPref");
//        Log.i("vikas", EventUtility.getHashString(userPassword, "SHA-1"));

    }

    public static void requestClubData(RequestQueue requestQueue, String clubListGetApi, final Context context) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, clubListGetApi,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("vikas", response.toString());
                        ClubParserer.parseClubJSON(response, context);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");
                    }
                }) {


        };
        requestQueue.add(request);
    }
}
