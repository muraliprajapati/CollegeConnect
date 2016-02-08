package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Murali on 18/01/2016.
 */
public class OtherEventView extends BaseActivity implements ObservableScrollViewCallbacks {

    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    Event event = null;
    Context context;
    EventDatabase eventDatabase;
    ArrayList<Event> listData;
    String clubName;
    ImageView mEventImage;
    TextView mEventName,mEventDayTime,mEventDayLeft,mEventAddressLineOne,mEventAddressLineTwo,
            mEventAddressLineThree,mEventOrganizerName,mEventorganizerMob,mEventOrganizerNameTwo,mEventorganizerMobTwo,
            mEventTitle;
    Toolbar toolbar;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    int position;
    private TextView mEventDescription;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbarView = findViewById(R.id.toolbar);

        String id = getIntent().getStringExtra("eventId");
        clubName = getIntent().getStringExtra("clubName");
        position = getIntent().getIntExtra("position", 0);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();

        context = this;
        eventDatabase = new EventDatabase(this);

        if(id != null){
            event = eventDatabase.selectByEventId(id);
        }else {
            event = getData(position);
        }


        mVolleySingleton = new VolleySingleton(this);
        mImageLoader = mVolleySingleton.getImageLoader();


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
        mEventTitle.setTextColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorWhite)));
        mScrollView = (ObservableScrollView) findViewById(R.id.event_scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);



        setEventData(event);


    }


    public Event getData(int position){
        listData = eventDatabase.selectByClub(clubName);
        //eventDatabase.selectByClubName(context,clubName);
        if(listData.size() == 0){
            return null;
        }
        return listData.get(position);
    }

    private void setEventData(Event eventData) {

        //Date date = new java.util.Date(Long.parseLong(String.valueOf(eventData.getEventStarttime())));
        ///String eventDateTime = new SimpleDateFormat("MMMM,dd,yyyy HH:mm a").format(date);
        String dateString = EventUtility.getFriendlyDayString(Long.parseLong(String.valueOf(eventData.getEventStarttime())));
        Long sdt = Long.valueOf(event.getEventStarttime());

        Calendar c = new GregorianCalendar();
        long daysLeft = sdt - c.getTimeInMillis();

        int TimeLeft = (int) daysLeft / (24* 1000 * 60 * 60);

        if(TimeLeft < 0){
            mEventDayLeft.setText("Event Already Happened");
        }else{
            mEventDayLeft.setText(TimeLeft + " Days From Now");
        }

        mEventName.setText(eventData.getEventTitle());
        mEventDayTime.setText(dateString);
        mEventAddressLineOne.setText(eventData.getEventVenue());
        mEventAddressLineTwo.setVisibility(View.GONE);
        mEventDescription.setText(eventData.getEventDescription());
        mEventOrganizerName.setText(eventData.getEventOrganizerOne());
        mEventorganizerMob.setText(eventData.getEventOrganizerOnePhoneNo());
        mEventOrganizerNameTwo.setText(eventData.getEventOrganizerTwo());
        mEventorganizerMobTwo.setText(eventData.getEventOrganizerTwoPhoneNo());
        mEventTitle.setText(eventData.getEventTitle());
        String urlThumnail = eventData.getUrlThumbnail();
        loadImages(urlThumnail, mEventImage);

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


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) (3 * scrollY) / mParallaxImageHeight);

        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        mEventTitle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, getResources().getColor(R.color.colorWhite)));

        ViewHelper.setTranslationY(mEventImage, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(OtherEventView.this, SlideShowActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
        }
        return true;
    }

    public void editEvent(Context context){
        Intent i = new Intent(context,CreateEventActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

}
