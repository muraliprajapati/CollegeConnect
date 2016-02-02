package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Murali on 02/02/2016.
 */
public class SparshEventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        RecyclerView SparshEventsRV = (RecyclerView) findViewById(R.id.myEventRecyclerView);
        SparshEventsRV.setLayoutManager(new LinearLayoutManager(this));
        SparshEventsRV.setAdapter(new SparshEventAdapter(this, ""));
    }
}
