package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.SqlDataListener;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Murali on 29/12/2015.
 */
public class EventView extends AppCompatActivity implements SqlDataListener {

    HttpAsyncTask task = null;
    Context context;
    Event eventData = null;
    EventDatabase eventDatabase;
    ArrayList<Event> listData;
    String clubName;
    ImageView mEventImage;
    TextView mEventName,mEventDayTime,mEventDayLeft,mEventAddressLineOne,mEventAddressLineTwo,
            mEventAddressLineThree,mEventOrganizerName,mEventorganizerMob,mEventOrganizerNameTwo,mEventorganizerMobTwo,
            mEventTitle;
    Toolbar toolbar;
    int position;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private TextView mEventDescription;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mVolleySingleton = VolleySingleton.getInstance(this);
        //mImageLoader = mVolleySingleton.getImageLoader();

        mEventImage = (ImageView) findViewById(R.id.event_image);
        mEventName = (TextView) findViewById(R.id.event_name);
        mEventDescription = (TextView) findViewById(R.id.event_description);
        mEventDayTime = (TextView) findViewById(R.id.event_daytime);
        mEventDayLeft = (TextView) findViewById(R.id.event_dayleft);
        mEventAddressLineOne = (TextView) findViewById(R.id.event_vanue_line_one);
        mEventAddressLineTwo = (TextView) findViewById(R.id.event_vanue_line_two);
        mEventOrganizerName = (TextView) findViewById(R.id.event_organizer_name);
        mEventorganizerMob = (TextView) findViewById(R.id.event_organizer_phone);
        mEventOrganizerNameTwo = (TextView) findViewById(R.id.event_organizer_name_two);
        mEventorganizerMobTwo = (TextView) findViewById(R.id.event_organizer_phone_two);
        mEventTitle = (TextView) findViewById(R.id.event_title);

        eventDatabase = new EventDatabase(this);

       // eventDatabase.insertRow("sparsh",121212,1212,1212,12,"IETE","for all","Murali Parjapati","Vikas"
        //                ,"SVNIT","87565464","87565464","vrnvikas1009@gmail.com","yes",true,1,"lasttimeNone");

       // eventDatabase.insertRow("Light Follower",121212,1212,1212,12,"DRISTI","for all","vikas","Siddhant loya"
        //        ,"SVNIT","87565464","45215456","vrnvikas1009@gmail.com","yes",true,1,"no time");

        clubName = getIntent().getStringExtra("clubName");
        position = getIntent().getIntExtra("position",0);
        context = this;

        eventData = getData(position);

        volleySingleton = VolleySingleton.getInstance(this);
        //requestQueue = volleySingleton.getRequestQueue();

        task = new HttpAsyncTask();
        task.execute();
        if(eventData != null){
            setEventData();
        }
    }


    public Event getData(int position){
        listData = eventDatabase.selectByClub(clubName);
        //eventDatabase.selectByClubName(context,clubName);
        if(listData.size() == 0){
            return null;
        }
        return listData.get(position);
    }

    private void setEventData() {

        Date date = new java.util.Date(Long.parseLong(String.valueOf(eventData.getEventStarttime())));
        String eventDateTime = new SimpleDateFormat("MMMM,dd,yyyy HH:mm a").format(date);
        mEventName.setText(eventData.getEventTitle());
        mEventDayTime.setText(eventDateTime);
        mEventAddressLineOne.setText(eventData.getEventVenue());
        mEventDescription.setText(eventData.getEventDescription());
        mEventOrganizerName.setText(eventData.getEventOrganizerOne());
        mEventorganizerMob.setText(eventData.getEventOrganizerOnePhoneNo());
        mEventOrganizerNameTwo.setText(eventData.getEventOrganizerOne());
        mEventorganizerMobTwo.setText(eventData.getEventOrganizerOnePhoneNo());
        mEventTitle.setText(eventData.getEventTitle());
        String urlThumnail = eventData.getUrlThumbnail();
        loadImages(urlThumnail, mEventImage);

    }

    @Override
    public void loadData(ArrayList<Event> data) {
        listData = data;
        eventData = listData.get(0);
        setEventData();
    }

    @Override
    public void loadEventById(Event event) {

    }


    private void loadImages(String urlThumbnail, final ImageView holder) {
        if (true) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.setImageBitmap(response.getBitmap());
                }
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
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
