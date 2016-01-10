package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.ModelClass.EventModel;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Murali on 29/12/2015.
 */
public class EventView extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    HttpAsyncTask task = null;
    Context context;
    EventModel eventData = null;
    EventDatabase eventDatabase;
    ArrayList<EventModel> listData;
    String clubName;

    ImageView mEventImage;
    TextView mEventName,mEventDayTime,mEventDayLeft,mEventAddressLineOne,mEventAddressLineTwo,
            mEventAddressLineThree,mEventOrganizerName,mEventorganizerMob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);
        mEventImage = (ImageView) findViewById(R.id.event_image);
        mEventName = (TextView) findViewById(R.id.event_name);
        mEventDayTime = (TextView) findViewById(R.id.event_daytime);
        mEventDayLeft = (TextView) findViewById(R.id.event_dayleft);
        mEventAddressLineOne = (TextView) findViewById(R.id.event_vanue_line_one);
        mEventAddressLineTwo = (TextView) findViewById(R.id.event_vanue_line_two);
        mEventAddressLineThree = (TextView) findViewById(R.id.event_vanue_line_three);
        mEventOrganizerName = (TextView) findViewById(R.id.event_organizer_name);
        mEventorganizerMob = (TextView) findViewById(R.id.event_organizer_phone);
        eventDatabase = new EventDatabase(this);
        eventDatabase.insertRow("sparsh","Tue,Jan 16 at 5:00PM","14:00","15:00","yes","IEEE","for all","yes","Murali Parjapati"
                        ,"SVNIT","87565464","vrnvikas1009@gmail.com","yes");

        eventDatabase.insertRow("Light Follower","Wed,Jan 17 at 5:30PM","14:00","15:00","yes","DRISTI","for all","yes","Siddhant loya"
                ,"SVNIT","87565464","vrnvikas1009@gmail.com","yes");

        eventDatabase.insertRow("SonicRace","Thur,Jan 18 at 6:30PM","14:00","15:00","yes","SAE","for all","yes","Rahul Mehra"
                ,"SVNIT","87565464","vrnvikas1009@gmail.com","yes");

        eventDatabase.insertRow("Code Bomber","Fri,Jan 19 at 7:30PM","14:00","15:00","yes","ACM","for all","yes","vikas chaudhary"
                ,"SVNIT","87565464","vrnvikas1009@gmail.com","yes");
        clubName = getIntent().getStringExtra("clubName");
        eventData = getData();

        context = this;
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();

        task = new HttpAsyncTask();
        task.execute();
        setEventData();

    }


    public EventModel getData(){

        EventModel event;
        listData = eventDatabase.selectByClub(clubName);
        event = listData.get(0);
        return event;
    }

    private void setEventData() {

        mEventName.setText(eventData.getEventName());
        mEventDayTime.setText(eventData.getEventDate());
        mEventAddressLineOne.setText(eventData.getEventVanue());
        mEventOrganizerName.setText(eventData.getEventoganizername());
        mEventorganizerMob.setText(eventData.getOrganizerMob());


    }

    private class HttpAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... urls) {
            Log.i("vikas", "AsyncTaskEvent");
            //JSONObject jsonObject =
               //     RequestorPost.requestJsonData(requestQueue, API.EVENT_API, "v", "v", context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //task.cancel(true);
        }
    }


}
