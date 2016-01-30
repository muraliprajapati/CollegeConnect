package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.androidviewhover.BlurLayout;

/**
 * Created by Murali on 18/01/2016.
 */
public class EventsByClubActivity extends AppCompatActivity {
    RecyclerView eventsByClubRV;
    View hover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_by_club);
        eventsByClubRV = (RecyclerView) findViewById(R.id.eventsByClubRecyclerView);
        CustomLayoutManager layoutManager = new CustomLayoutManager(this);
        eventsByClubRV.setLayoutManager(layoutManager);
        String clubName = getIntent().getStringExtra("clubName");


        eventsByClubRV.setAdapter(new MyEventsAdapter(this, clubName));
        eventsByClubRV.setNestedScrollingEnabled(false);

    }
}
