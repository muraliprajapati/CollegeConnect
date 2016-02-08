package com.sophomoreventure.collegeconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.sophomoreventure.collegeconnect.Activities.DrawerActivity;

/**
 * Created by Murali on 02/02/2016.
 */
public class NoticeBoardActivity extends DrawerActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.council_notice_layout);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayoutCouncil);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(NoticeBoardActivity.this)
                        .setTitle("Redesign of website")
                        .setMessage(R.string.council)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(NoticeBoardActivity.this, activity));
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
