package com.sophomoreventure.collegeconnect.JsonHandler;

import android.content.Context;
import android.util.Log;

import com.sophomoreventure.collegeconnect.ModelClass.ClubModel;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Vikas Kumar on 01-02-2016.
 */
public class ClubParserer {

    public static ArrayList<ClubModel> parseClubJSON(JSONObject response,Context context) {
        ArrayList<ClubModel> clubList= new ArrayList<>();
        ClubsDataBase dataBase = new ClubsDataBase(context);
        StringBuilder builderName = new StringBuilder();
        StringBuilder builderMobNo = new StringBuilder();
        StringBuilder builderEmail = new StringBuilder();


        try {
            JSONArray jsonArray = response.getJSONArray("clubs");
            for(int i = 0; i < jsonArray.length(); i++ ){
                Log.i("vikas","club data parserer");
                JSONObject currentClub = jsonArray.getJSONObject(i);
                String about = currentClub.getString("about");
                JSONArray adminsList = currentClub.getJSONArray("admins");
                for(int j =0; i < adminsList.length();i++){

                    JSONObject current = adminsList.getJSONObject(j);
                    String email = current.getString("email");
                    String name = current.getString("name");
                    //int mobNo = current.getInt("mobno");
                    builderName.append(email);
                    //builderMobNo.append(mobNo);
                    builderEmail.append(email);
                }

                int id = currentClub.getInt("club_id");
                String clubName = currentClub.getString("name");
                dataBase.insertRow(String.valueOf(id), clubName, about, builderName.toString(),
                        builderMobNo.toString(), builderEmail.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("vikas", e + "");
        }


        return clubList;
    }

}
