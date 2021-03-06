package com.sophomoreventure.collegeconnect.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sophomoreventure.collegeconnect.CustomLayoutManager;
import com.sophomoreventure.collegeconnect.adapters.MyEventsAdapter;
import com.sophomoreventure.collegeconnect.R;

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
        eventsByClubRV.setLayoutManager(new LinearLayoutManager(this));
        String clubName = getIntent().getStringExtra("clubName");
        int position = getIntent().getIntExtra("position",0);
        eventsByClubRV.setAdapter(new MyEventsAdapter(this, clubName,position));
        eventsByClubRV.setNestedScrollingEnabled(false);

    }

    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EventsByClubActivity.this, activity));
            }
        }, 260);
    }
}
