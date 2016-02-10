package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;

/**
 * Created by Murali on 05/01/2016.
 */
public class MyEventsActivity extends DrawerBaseActivity {
    RecyclerView myEventsRV;
    EventDatabase eventDatabase;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setTitle("");
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

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_MY_EVENTS;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
