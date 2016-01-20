package com.sophomoreventure.collegeconnect.Network;

import android.content.Context;
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
}
