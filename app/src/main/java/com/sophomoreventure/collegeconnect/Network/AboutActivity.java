package com.sophomoreventure.collegeconnect.Network;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sophomoreventure.collegeconnect.BuildConfig;
import com.sophomoreventure.collegeconnect.DrawerBaseActivity;
import com.sophomoreventure.collegeconnect.R;

/**
 * Created by Murali on 07/02/2016.
 */
public class AboutActivity extends DrawerBaseActivity implements View.OnClickListener {
    TextView versionCodeTextView;
    RelativeLayout facebookLayout, emailLayout;
    String facebookPageUrl = "https://www.facebook.com/College-Connect-1038784402811242/timeline";
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_about);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setTitle("About us");
        versionCodeTextView = (TextView) findViewById(R.id.versionTextView);
        versionCodeTextView.setText(BuildConfig.VERSION_NAME);
        facebookLayout = (RelativeLayout) findViewById(R.id.facebook_layout);
        emailLayout = (RelativeLayout) findViewById(R.id.email_layout);

        facebookLayout.setOnClickListener(this);
        emailLayout.setOnClickListener(this);
        overridePendingTransition(0, 0);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_ABOUT;
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.facebook_layout:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageUrl));
                startActivity(browserIntent);
                break;
            case R.id.email_layout:
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:" + "college.connect01@gmail.com"));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
