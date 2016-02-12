package com.sophomoreventure.collegeconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sophomoreventure.collegeconnect.adapters.MyEventsAdapter;

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
        String clubName = getIntent().getStringExtra("clubName");
        int position = getIntent().getIntExtra("position",0);
        SparshEventsRV.setAdapter(new MyEventsAdapter(this,clubName,position));

    }

    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SparshEventActivity.this, activity));
            }
        }, 260);
    }
}
