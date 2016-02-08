package com.sophomoreventure.collegeconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.sophomoreventure.collegeconnect.Activities.DrawerActivity;

/**
 * Created by Murali on 18/01/2016.
 */
public class EventsByClubActivity extends DrawerActivity implements NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        menuItem.setChecked(false);

        switch (id) {

            case R.id.nav_sparsh_events:
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                launchActivityDelayed(SparshEventListAtivity.class);
                break;
            case R.id.nav_events:
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_clubs:
                mDrawerLayout.closeDrawers();
                launchActivityDelayed(ClubListAtivity.class);
                menuItem.setChecked(true);
                break;

            case R.id.nav_notice_board:
                mDrawerLayout.closeDrawers();
                launchActivityDelayed(NoticeBoardActivity.class);
                menuItem.setChecked(true);
                break;

            case R.id.nav_myenents:
                mDrawerLayout.closeDrawers();
                launchActivityDelayed(MyEventsActivity.class);
                menuItem.setChecked(true);
                break;
            case R.id.nav_myprofile:
                menuItem.setChecked(true);
                return false;
            case R.id.nav_settings:
                menuItem.setChecked(true);
                return false;
            case R.id.nav_rate:
                menuItem.setChecked(true);
                return false;

        }

        return true;
    }
}
