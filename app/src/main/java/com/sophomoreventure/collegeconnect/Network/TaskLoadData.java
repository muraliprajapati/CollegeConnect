package com.sophomoreventure.collegeconnect.Network;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public class TaskLoadData extends AsyncTask<Void, Void, ArrayList<String>> {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private DataListener listener;


    public TaskLoadData(DataListener listener){
        this.listener = listener;
        //volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {

        //JSONObject response = RequestorPost.requestStringData(requestQueue, "https://sheltered-fjord-8731.herokuapp.com/api/user/reg");

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<String> listMovies) {
        if (listener != null) {
            //listener.onDataLoaded(listMovies);
        }
    }
}
