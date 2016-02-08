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
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.sophomoreventure.collegeconnect.CustomLayoutManager;
import com.sophomoreventure.collegeconnect.DrawerBaseActivity;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.HorizontalRecyclerAdapter;
import com.sophomoreventure.collegeconnect.MyEventsAdapter;
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

public class SlideShowActivity extends DrawerBaseActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    private static final long POLL_FREQUENCY = 5000;//28800000;
    private static final int JOB_ID = 100;
    ViewPager slideShowPager;
    Toolbar toolbar;
    int currentPage = 0;
    int[] imageResArray = new int[]{R.drawable.poster_five, R.drawable.poster_four, R.drawable.poster_three, R.drawable.poster_two, R.drawable.poster_three};    //a layout grouping the toolbar and the tabs together
    //private ViewGroup mContainerToolbar;
    private FragmentDrawer mDrawerFragment;
    private JobScheduler mJobScheduler;
    private NavigationView mNavView;
    private LinearLayout mainScreen;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView horizonatalRV;
    private RecyclerView aRV;
    private GoogleApiClient client;
    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slide_show);
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ((AppBarLayout) findViewById(R.id.app_bar)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    swipeRefreshLayout.setEnabled(false);
                    collapsingToolbarLayout.setTitle("College Connect");
                    isShow = true;
                } else if (isShow) {
                    swipeRefreshLayout.setEnabled(false);
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }

                if (collapsingToolbarLayout.getHeight() + verticalOffset < 4 * ViewCompat.getMinimumHeight(collapsingToolbarLayout)) {
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setEnabled(true);
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setNestedScrollingEnabled(true);

        final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());
        slideShowPager = (ViewPager) findViewById(R.id.slideShowPager);
        final SlideShowAdapter slideAdapter = new SlideShowAdapter(getSupportFragmentManager());
        slideShowPager.setAdapter(slideAdapter);

        slideShowPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return true;
            }
        });


        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(slideShowPager);
        slideShowPager.addOnPageChangeListener(this);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == slideShowPager.getAdapter().getCount()) {
                            currentPage = 0;
                        }
                        slideShowPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, 1500, 1500);

        horizonatalRV = (RecyclerView) findViewById(R.id.byClubRecyclerView);
        horizonatalRV.setNestedScrollingEnabled(false);
        horizonatalRV.setLayoutManager(new LinearLayoutManager(SlideShowActivity.this, LinearLayoutManager.HORIZONTAL, false));
        horizonatalRV.setAdapter(new HorizontalRecyclerAdapter(SlideShowActivity.this));

//        RelativeLayout.LayoutParams params = new
//                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//
//        params.height=EventUtility.dpToPx(256,getResources())*6;

        aRV = (RecyclerView) findViewById(R.id.browseEventsRecyclerView);
        aRV.setNestedScrollingEnabled(false);
//        aRV.setLayoutParams(params);
        CustomLayoutManager layoutManager = new CustomLayoutManager(aRV);
        layoutManager.setChildSize(EventUtility.dpToPx(256, getResources()));
        aRV.setLayoutManager(layoutManager);
//        aRV.setLayoutManager(new LinearLayoutManager(this));
        aRV.setHasFixedSize(true);

        aRV.setAdapter(new MyEventsAdapter(SlideShowActivity.this, "", 0));


    }



    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_COLLEGE_EVENTS;
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
        swipeRefreshLayout.setEnabled(false);
        // Toast.makeText(getApplicationContext(),position + " page scrolled",Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onBackPressed() {
//        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            mDrawerLayout.closeDrawer(Gravity.LEFT);
//        } else {
//            finish();
//
//        }
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        Toolbar toolbar = getSupportActionBar();
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);
        return true;

    }



    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        mNavView.setCheckedItem(R.id.nav_events);
    }

    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SlideShowActivity.this, activity));
            }
        }, 260);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(SlideShowActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                EventUtility.clearUserSharedPref(this);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                break;
        }
        return false;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Your Code here
            Toast.makeText(SlideShowActivity.this, "" + currentPage, Toast.LENGTH_SHORT).show();
            return true;

        }
    }
}



