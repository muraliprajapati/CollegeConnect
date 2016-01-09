package com.sophomoreventure.collegeconnect.Activities;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.CreateEventActivity;
import com.sophomoreventure.collegeconnect.EventView;
import com.sophomoreventure.collegeconnect.MyEventsActivity;
import com.sophomoreventure.collegeconnect.Network.ServiceClass;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.fragments.FragmentDrawer;
import com.sophomoreventure.collegeconnect.fragments.SlideShowFragment;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


/**
 * Created by Murali on 08/12/2015.
 */

public class SlideShowActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener ,NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final long POLL_FREQUENCY = 28800000;
    private static final int JOB_ID = 100;
    ViewPager slideShowPager;
    SlideShowAdapter adapter;
    Timer timer;
    Toolbar toolbar;
    int currentPage = 0;
    int[] imageResArray = new int[]{R.drawable.pixeldropr, R.drawable.bird, R.drawable.pixel};
    //a layout grouping the toolbar and the tabs together
    //private ViewGroup mContainerToolbar;
    private FragmentDrawer mDrawerFragment;
    private JobScheduler mJobScheduler;
    private NavigationView mNavView;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mainScreen;
    private TextView mIEEEtextview,mDristiTextView,mSAETextView,mACMTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mIEEEtextview = (TextView) findViewById(R.id.ieee_textview);
        mDristiTextView = (TextView) findViewById(R.id.dristi_textview);
        mSAETextView = (TextView) findViewById(R.id.sae_textview);
        mACMTextView = (TextView) findViewById(R.id.acm_textview);

        mIEEEtextview.setOnClickListener(this);
        mACMTextView.setOnClickListener(this);
        mDristiTextView.setOnClickListener(this);
        mSAETextView.setOnClickListener(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("College Connect");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        setupDrawer();
        //setupJob();
        mainScreen = (LinearLayout) findViewById(R.id.main_screen);
        FragmentManager manager = getSupportFragmentManager();
        adapter = new SlideShowAdapter(manager);
        slideShowPager = (ViewPager) findViewById(R.id.slideShowPager);
        slideShowPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(slideShowPager);
        slideShowPager.addOnPageChangeListener(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == adapter.getCount()) {
                            currentPage = 0;
                        }
                        slideShowPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, 500, 1500);

    }


    private void setupDrawer() {

        //mToolbar = (Toolbar) findViewById(R.id.custom_app_bar);
        //mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        //setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mNavView = (NavigationView) findViewById(R.id.navView);
        mNavView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle dt = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                0, 0
        );
        mDrawerLayout.setDrawerListener(dt);
        dt.syncState();
        /*
        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        */
    }


    private void translateMainScreen(float slideOffset) {
        if (mainScreen != null) {

            mainScreen.setTranslationX(slideOffset * 550);
        }
    }




    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                buildJob();
            }
        }, 30000);
    }

    private void buildJob() {

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceClass.class));
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Toast.makeText(getApplicationContext(),position + " page scrolled",Toast.LENGTH_SHORT).show();
        Log.i("TAG", "in OnPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
//        Toast.makeText(getApplicationContext(),position + " page selected",Toast.LENGTH_SHORT).show();
//        Log.i("TAG", "in OnPageSelected");
    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.login_activity){

            startActivity(new Intent(this,LoginActivity.class));
        }

        if (id == R.id.create_event_activity) {

            startActivity(new Intent(this, CreateEventActivity.class));
        }

        if (id == R.id.event_view_activity) {

            startActivity(new Intent(this, EventView.class));
        }

        if (id == R.id.my_event_activity) {

            startActivity(new Intent(this, MyEventsActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

            switch (id) {
                case R.id.nav_events:
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    break;
                case R.id.nav_clubs:
                    menuItem.setChecked(true);
                    return false;
                case R.id.nav_myenents:
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == mIEEEtextview.getId()){
            Intent i = new Intent(v.getContext(),EventView.class);
            i.putExtra("clubName","IEEE");
            startActivity(i);
        }

        if(id == mACMTextView.getId()){
            Intent i = new Intent(v.getContext(),EventView.class);
            i.putExtra("clubName","ACM");
            startActivity(i);
        }

        if(id == mSAETextView.getId()){
            Intent i = new Intent(v.getContext(),EventView.class);
            i.putExtra("clubName","SAE");
            startActivity(i);
        }

        if(id == mDristiTextView.getId()){
            Intent i = new Intent(v.getContext(),EventView.class);
            i.putExtra("clubName","DRISTI");
            startActivity(i);
        }
    }


    class SlideShowAdapter extends FragmentStatePagerAdapter {

        public SlideShowAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return SlideShowFragment.newInstance(imageResArray[position]);
        }

        @Override
        public int getCount() {
            return imageResArray.length;
        }
    }
}


