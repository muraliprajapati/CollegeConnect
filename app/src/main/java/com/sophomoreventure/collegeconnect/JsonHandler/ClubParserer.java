package com.sophomoreventure.collegeconnect.JsonHandler;

import android.content.Context;
import android.util.Log;

import com.sophomoreventure.collegeconnect.ModelClass.ClubModel;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.extras.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_ABOUT;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_ADMIN;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_CLUBS;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_CLUB_ID;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_CLUB_NAME;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_EMAIL;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_MOBNOB;
import static com.sophomoreventure.collegeconnect.extras.Keys.EndpointClub.KEY_NAME;

/**
 * Created by Vikas Kumar on 01-02-2016.
 */
public class ClubParserer {

    public static ArrayList<ClubModel> parseClubJSON(JSONObject response, Context context) {
        ArrayList<ClubModel> clubList = new ArrayList<>();
//        ClubsDataBase dataBase = new ClubsDataBase(context);
        ClubsDataBase dataBase = ClubsDataBase.newInstance(context);
        dataBase.deleteDatabase();

        if (response != null && response.length() > 0) {
            try {

                JSONArray jsonArray = response.getJSONArray(KEY_CLUBS);

                if (jsonArray.length() != 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String adminName = "";
                        String adminMobNo = "";
                        String adminEmail = "";
                        String about = Constants.NA;
                        String clubName = Constants.NA;
                        String email = Constants.NA;
                        String ClubId = Constants.NA;
                        String ClubImage = Constants.NA;

                        JSONObject currentClub = jsonArray.getJSONObject(i);

                        if (Utils.contains(currentClub, KEY_ABOUT)) {
                            about = currentClub.getString(KEY_ABOUT);
                        }


                        if (Utils.contains(currentClub, KEY_CLUB_ID)) {
                            ClubId = String.valueOf(currentClub.getInt(KEY_CLUB_ID));
                        }

                        if (Utils.contains(currentClub, KEY_CLUB_NAME)) {
                            clubName = currentClub.getString(KEY_CLUB_NAME);
                        }

                        if(Utils.contains(currentClub,"image")){
                            ClubImage = currentClub.getString("image");
                        }

                        JSONArray adminsList = currentClub.getJSONArray(KEY_ADMIN);

                        for (int j = 0; j < adminsList.length(); j++) {
                            JSONObject current = adminsList.getJSONObject(j);

                            if (Utils.contains(current, KEY_EMAIL)) {
                                adminEmail = adminEmail + "?" +current.getString(KEY_EMAIL);
                            }
                            if (Utils.contains(current, KEY_NAME)) {
                                adminName = adminName + "?" + current.getString(KEY_NAME);
                            }
                            if (Utils.contains(current, KEY_MOBNOB)) {
                                adminMobNo =  adminMobNo+ "?"+ String.valueOf(current.getInt(KEY_MOBNOB));
                            }

                        }

                        Log.i("expose","admin email" +clubName +"  "+ adminEmail + " " + adminName +" " + adminMobNo);
                        dataBase.insertRow(ClubId, clubName, about, adminName, adminMobNo, adminEmail, ClubImage);
                        adminEmail = "";
                        adminName = "";
                        adminMobNo = "";
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("vikas","Json exception Clubs" + e);
            }


        }

        return clubList;
    }

}
