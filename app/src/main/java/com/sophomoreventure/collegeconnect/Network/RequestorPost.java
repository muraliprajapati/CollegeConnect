package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.EventUtility;

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
                Log.i("vikas", error+"");
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

    //    public static JSONObject requestJsonData(
//            final RequestQueue requestQueue, String url, final String userName,
//            final String userPassword, final JSONObject jsonBody, final Context context) {
//
//
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonBody,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("vikas", response.toString());
//                        jsonObject = response;
////                        Parserer.parseResponse(response,context);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("vikas", error + "");
//                        NetworkResponse response = error.networkResponse;
//                        Log.i("vikas", response.statusCode + "");
//                        String string = new String(response.data);
//                        try {
//                            JSONObject jsonObject = new JSONObject(string);
//                            Log.i("vikas", response.statusCode + ":" + jsonObject.toString());
//                            ;
//                            DataListener listener = (DataListener) context;
//                            listener.setError(Parserer.parseErrorResponse(jsonObject));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }){
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", userName, userPassword);
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
//                params.put("Authorization", auth);
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//
//        requestQueue.add(request);
//        return jsonObject;
//
//    }
    public static JSONObject requestRegistration(
            final RequestQueue requestQueue, String url, final String userName,
            final String userPassword, final JSONObject jsonBody, final boolean svnitian, final Context context) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        jsonObject = response;
                        try {
                            parseAndSaveUserInfoToPref(context, userName, userPassword, Parserer.parseToken(response), jsonBody.toString(), svnitian);
                            Intent intent = new Intent(context, SlideShowActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                    }
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
                            listener.setError(Parserer.parseErrorResponse(jsonObject));

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

    private static void parseAndSaveUserInfoToPref(Context context, String userName, String userPassword, String token, String jsonString, boolean svnitian) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String emailId = jsonObject.getString("email");
        String mobNo = jsonObject.getString("mobno");
        String rollNo = jsonObject.getString("rollno");

        SharedPreferences prefs = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, true);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, userName);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, EventUtility.getHashString(userPassword, "SHA-1"));
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, token);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_EMAIL_KEY, emailId);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_ROLL_NO_KEY, rollNo);
        if (mobNo != null) {
            editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_MOB_NO_KEY, mobNo);
        }
        editor.apply();
        Log.i("tag", token);
        Log.i("tag", EventUtility.getHashString(userPassword, "SHA-1"));

    }

}
