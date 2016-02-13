package com.sophomoreventure.collegeconnect.Activities;

import android.content.ComponentName;
import android.content.Context;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sophomoreventure.collegeconnect.CustomLayoutManager;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.EventsUtils;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.ServiceClass;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.OtherEventView;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.adapters.HorizontalRecyclerAdapter;
import com.sophomoreventure.collegeconnect.adapters.MyEventsAdapter;
import com.sophomoreventure.collegeconnect.extras.API;
import com.sophomoreventure.collegeconnect.fragments.SlideShowFragment;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by Murali on 08/12/2015.
 */

public class SlideShowActivity extends DrawerBaseActivity implements
        ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    private static final long POLL_FREQUENCY = 5000;//28800000;
    private static final int JOB_ID = 100;
    ViewPager slideShowPager;
    Toolbar toolbar;
    int currentPage = 0;
    //a layout grouping the toolbar and the tabs together
    //private ViewGroup mContainerToolbar;
    EventDatabase database;
    ArrayList<Event> listDataSlideShow;
    Handler handler;
    Runnable pageChangeRunnable;
    private JobScheduler mJobScheduler;
    private NavigationView mNavView;
    private LinearLayout mainScreen;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView horizonatalRV;
    private RecyclerView aRV;
    private GoogleApiClient client;
    private DrawerLayout mDrawerLayout;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slide_show);
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();
        EventsUtils.loadEventsData(requestQueue, this);

        database = new EventDatabase(this);
        listDataSlideShow = database.viewSlideShowData();
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
//                    swipeRefreshLayout.setEnabled(false);
                    collapsingToolbarLayout.setTitle("College Connect");
                    isShow = true;
                } else if (isShow) {
//                    swipeRefreshLayout.setEnabled(false);
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        final GestureDetector tapGestureDetector = new GestureDetector(this, new TapGestureListener());
        slideShowPager = (ViewPager) findViewById(R.id.slideShowPager);
        slideShowPager.setAdapter(new SlideShowAdapter(getSupportFragmentManager(), this));

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

/*
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

        */
        handler = new Handler();

        pageChangeRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == slideShowPager.getAdapter().getCount()) {
                    currentPage = 0;
                }
                slideShowPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 1500);
            }
        };


        handler.postDelayed(pageChangeRunnable, 1500);

        horizonatalRV = (RecyclerView) findViewById(R.id.byClubRecyclerView);
        horizonatalRV.setNestedScrollingEnabled(false);
        horizonatalRV.setLayoutManager(new LinearLayoutManager(SlideShowActivity.this, LinearLayoutManager.HORIZONTAL, false));
        horizonatalRV.setAdapter(new HorizontalRecyclerAdapter(SlideShowActivity.this));


        MyEventsAdapter adapter = new MyEventsAdapter(SlideShowActivity.this, "SlideShowView", 0);
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        params.height = EventUtility.dpToPx(256, getResources()) * adapter.getItemCount();

        aRV = (RecyclerView) findViewById(R.id.browseEventsRecyclerView);
        aRV.setNestedScrollingEnabled(false);
        aRV.setLayoutParams(params);
        CustomLayoutManager layoutManager = new CustomLayoutManager(aRV);
//        layoutManager.setChildSize(EventUtility.dpToPx(256, getResources()));
//        aRV.setLayoutManager(layoutManager);
        aRV.setLayoutManager(new LinearLayoutManager(this));
        aRV.setHasFixedSize(true);


        aRV.setAdapter(adapter);

        overridePendingTransition(0, 0);
    }


    @Override
    protected void onStop() {
        super.onStop();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

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
        Log.i("tag", "in OnResume");
        handler.postDelayed(pageChangeRunnable, 1500);
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

        RequestorGet.requestEventData(requestQueue, API.EVENT_API, this);
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("tag", "in OnPause");
        handler.removeCallbacks(pageChangeRunnable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("tag", "in OnRestart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("tag", "in OnPostReume");
    }

    class SlideShowAdapter extends FragmentStatePagerAdapter {
        Context context;


        public SlideShowAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;


        }

        @Override
        public Fragment getItem(int position) {

            return SlideShowFragment.newInstance(position, context);
        }

        @Override
        public int getCount() {
            return 4;

        }
    }

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Your Code here

            Intent intent = new Intent(SlideShowActivity.this, OtherEventView.class);
            if (listDataSlideShow.size() != 0) {
                if(listDataSlideShow.size() > 4){
                    intent.putExtra("clubName", listDataSlideShow.get(currentPage).getEventClub());
                    intent.putExtra("eventId", listDataSlideShow.get(currentPage).getEventServerId());
                    intent.putExtra("position", currentPage);
                    SlideShowActivity.this.startActivity(intent);
                }

            } else {
                Toast.makeText(SlideShowActivity.this, "No Event To Display", Toast.LENGTH_SHORT).show();
            }

            return true;

        }

    }
}
