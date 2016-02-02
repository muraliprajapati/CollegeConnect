package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Murali on 05/01/2016.
 */
public class MyEventsActivity extends AppCompatActivity {
    RecyclerView myEventsRV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        myEventsRV = (RecyclerView) findViewById(R.id.myEventRecyclerView);
        myEventsRV.setLayoutManager(new LinearLayoutManager(this));
        myEventsRV.setAdapter(new ClubEventsAdapter(this, ""));
    }
}
