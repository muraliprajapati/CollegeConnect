package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

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
import com.android.volley.toolbox.StringRequest;
import com.sophomoreventure.collegeconnect.extras.Constants;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.HttpsTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class RequestorPost {

    static boolean dataResponse = false;
    static JSONObject jsonObject = null;

    public static boolean requestStringData(final RequestQueue requestQueue, String url, final String userName, final String userPassword) {

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vikas", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("vikas", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                String creds = String.format("%s:%s", userName, userPassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(sr);
        return dataResponse;
    }

    public static JSONObject requestJsonData(
            final RequestQueue requestQueue, final String url, final String userName,
            final String userPassword, final Context context) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        jsonObject = response;
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
                            DataListener listener = (DataListener) context;
                            listener.setError(url, Parserer.parseResponse(jsonObject));

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


    public static JSONObject requestRegistration(
            final RequestQueue requestQueue, final String url, final String email,
            final String userPassword, final JSONObject jsonBody, final Context context) {

        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        jsonObject = response;
                        try {

                            parseAndSaveUserInfoToPref(context, email, userPassword, Parserer.parseToken(response), jsonBody);

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
                String creds = String.format("%s:%s", email, userPassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(request);
        return jsonObject;

    }

    public static void requestCreateEvent(
            final RequestQueue requestQueue, final String url, final String email,
            final String userPassword, final JSONObject jsonBody, final Context context) {
        Log.i("vikas", "in requestCreateEvent");

        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        jsonObject = response;
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
                String creds = String.format("%s:%s", email, userPassword);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(request);

    }

    public static void requestGcmToken(
            final RequestQueue requestQueue, final String url, final JSONObject jsonBody, final Context context) {
        Log.i("vikas", "in requestGCMToken");

        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
//                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());

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
                            Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
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


        };
        requestQueue.add(request);

    }
    private static void saveTokenInPref(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(
                Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, true);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, token);
        editor.apply();

    }

    public static void requestForgotPassword(
            final RequestQueue requestQueue, final String url, final JSONObject jsonBody, final Context context) {

        HttpsTrustManager.allowAllSSL();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        listener.onDataLoaded(url);

                    }
                },
                new Response.ErrorListener() {
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
                                if (response.statusCode == 404) {
                                    listener.setError(url, "Email is not registered");

                                }
//                                listener.setError(Parserer.parseResponse(jsonObject));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                }) {


        };
        requestQueue.add(request);

    }


    public static void attendRequest(final RequestQueue requestQueue, String url,final Context context) {

        final String emailId = EventUtility.getUserTokenFromPref(context);
        final String pass = EventUtility.getUserPasswordHashFromPref(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");

                        //DataListener listener = (DataListener) context;
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            //  listener.setError("NOCON","");

                        } else {
                            try {
                                NetworkResponse response = error.networkResponse;
                                Log.i("vikas", response.statusCode + "");
                                String string = new String(response.data);
                                JSONObject jsonObject = new JSONObject(string);
                                Log.i("vikas", response.statusCode + ":" + jsonObject.toString());
                                //    listener.setError(Parserer.parseResponse(jsonObject),"");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", emailId,pass);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        requestQueue.add(request);
    }

    private static void parseAndSaveUserInfoToPref(Context context, String userEmail, String userPassword, String token, JSONObject jsonObject) throws JSONException {
        String rollNo, hostelName;
        long mobNo;
        boolean hostelite;
        String name = jsonObject.getString("name");


        SharedPreferences prefs = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, true);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_EMAIL_KEY, userEmail);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, userPassword);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, token);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, name);


        if (jsonObject.has("mobno")) {
            mobNo = jsonObject.getLong("mobno");
            editor.putLong(Constants.SharedPrefConstants.USER_SHARED_PREF_MOB_NO_KEY, mobNo);
        }

        if (jsonObject.has("rollno")) {
            rollNo = jsonObject.getString("rollno");
            editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_ROLL_NO_KEY, rollNo);
        }

        if (jsonObject.has("hostel_or_local")) {
            hostelite = jsonObject.getBoolean("hostel_or_local");
            editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_HOSTELITE_KEY, hostelite);
        }

        if (jsonObject.has("hostelname")) {
            hostelName = jsonObject.getString("hostelname");
            editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_HOSTEL_NAME_KEY, hostelName);
        }

        org.json.JSONArray jsonArray = jsonObject.getJSONArray("liked");


        editor.apply();
        Log.i("tag", token);
        Log.i("tag", EventUtility.getHashString(userPassword, "SHA-1"));

    }

}
