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
import com.android.volley.toolbox.RequestFuture;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.EventUtility;

import org.json.JSONException;
import org.json.JSONObject;

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

//    public static JSONObject requestDataJSON(RequestQueue requestQueue, String url) {
//        JSONObject response = null;
//        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
//                url, (String) null, requestFuture, requestFuture);
//
//        requestQueue.add(request);
//        try {
//            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }

    public static void requestLogin(
            final RequestQueue requestQueue, String url, final String userName,
            final String userPassword, final Context context) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    DataListener listener = (DataListener) context;
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("vikas", response.toString());
                        String token = Parserer.parseToken(response);
                        try {
                            parseAndSaveToPref(context, userName, userPassword, token);
                            listener.onDataLoaded(true);
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
                    DataListener listener = (DataListener) context;
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("vikas", error + "");
                        NetworkResponse response = error.networkResponse;
                        Log.i("vikas", response.statusCode + "");
                        String string = new String(response.data);
                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            Log.i("vikas", response.statusCode + ":" + jsonObject.toString());


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


    }

    private static void parseAndSaveToPref(Context context, String userName, String userPassword, String token) throws JSONException {

        SharedPreferences prefs = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, true);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, userName);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, EventUtility.getHashString(userPassword, "SHA-1"));
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, token);
        editor.apply();
        Log.i("tag", token);
        Log.i("tag", EventUtility.getHashString(userPassword, "SHA-1"));

    }
}
