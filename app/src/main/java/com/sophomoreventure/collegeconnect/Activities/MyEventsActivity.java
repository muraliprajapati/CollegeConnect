package com.sophomoreventure.collegeconnect.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.adapters.MyEventsAdapter;
import com.sophomoreventure.collegeconnect.R;

/**
 * Created by Murali on 05/01/2016.
 */
public class MyEventsActivity extends AppCompatActivity {
    RecyclerView myEventsRV;
    EventDatabase eventDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        eventDatabase = new EventDatabase(this);
        myEventsRV = (RecyclerView) findViewById(R.id.myEventRecyclerView);
        myEventsRV.setLayoutManager(new LinearLayoutManager(this));
        String clubName = getIntent().getStringExtra("clubName");
        int position = getIntent().getIntExtra("position",0);
        if(clubName == null){
            clubName = "null";
        }

        myEventsRV.setAdapter(new MyEventsAdapter(this, clubName,0));

    }
}
