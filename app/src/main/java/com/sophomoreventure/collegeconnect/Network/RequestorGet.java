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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.JsonHandler.ClubParserer;
import com.sophomoreventure.collegeconnect.ParserEventResponse;

import org.json.JSONException;
import org.json.JSONObject;

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


    public static void attendRequest(final RequestQueue requestQueue, String url,final Context context) {

        SharedPreferences prefs = context.getSharedPreferences(
                Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        final String userId = prefs.getString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, "null");
        final String userPassId = prefs.getString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY,"null");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");

                        DataListener listener = (DataListener) context;
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            listener.setError("NOCON");

                        } else {
                            try {
                                NetworkResponse response = error.networkResponse;
                                Log.i("vikas", response.statusCode + "");
                                String string = new String(response.data);
                                JSONObject jsonObject = new JSONObject(string);
                                Log.i("vikas", response.statusCode + ":" + jsonObject.toString());
                                listener.setError(Parserer.parseResponse(jsonObject));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", userId,userPassId);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        requestQueue.add(request);

    }

    public static void requestLogin(
            final RequestQueue requestQueue, final String url, final String userEmail,
            final String userPassword, final Context context) {
        Log.i("vikas", "in Login Request");

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

                        } else if (error instanceof ServerError) {
                            listener.setError(url, "SERVERERR");

                        } else {
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


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        try {
                            parseAndSaveUserInfoToPref(context, response);
                            listener.onDataLoaded(url);
                        } catch (JSONException e) {
                            Log.i("vikas", "" + e);
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
        editor.apply();

        Log.i("vikas", token);
        Log.i("vikas", EventUtility.getHashString(userPassword, "SHA-1"));

    }

    private static void parseAndSaveUserInfoToPref(Context context, JSONObject response) throws JSONException {
        Log.i("vikas", "parseAndSaveUserInfoToPref" + response.toString());
        String email = response.getString("email");
        String name = response.getString("name");
        String mobNo = response.getString("mobno");
        String rollNo = response.getString("rollno");

        Log.i("vikas", "user info:" + email);
        Log.i("vikas", "user info:" + name);
        Log.i("vikas", "user info:" + rollNo);
        Log.i("vikas", "user info:" + mobNo);

        SharedPreferences prefs = context.getSharedPreferences(
                Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_EMAIL_KEY, email);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, name);
        if (!mobNo.equalsIgnoreCase("null") && !mobNo.isEmpty()) {

            editor.putLong(Constants.SharedPrefConstants.USER_SHARED_PREF_MOB_NO_KEY, Long.parseLong(mobNo));
        }

        if (!rollNo.equalsIgnoreCase("null") && !rollNo.isEmpty()) {
            rollNo = response.getString("rollno");
            editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_ROLL_NO_KEY, rollNo);
        }
        editor.apply();
        Log.i("vikas", "parseAndSaveUserInfoToPref");
//        Log.i("vikas", EventUtility.getHashString(userPassword, "SHA-1"));

    }

    public static void requestClubData(RequestQueue requestQueue, String clubListGetApi, final Context context) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, clubListGetApi,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        Log.i("vikas", response.toString());
                        ClubParserer.parseClubJSON(response,context);

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
