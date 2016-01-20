package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Murali on 18/01/2016.
 */
public class EventsByClubActivity extends AppCompatActivity {
    RecyclerView eventsByClubRV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_by_club);
        eventsByClubRV = (RecyclerView) findViewById(R.id.eventsByClubRecyclerView);
        CustomLayoutManager layoutManager = new CustomLayoutManager(this);
        eventsByClubRV.setLayoutManager(layoutManager);
        eventsByClubRV.setAdapter(new MyEventsAdapter(this));
        eventsByClubRV.setNestedScrollingEnabled(false);
    }
}
