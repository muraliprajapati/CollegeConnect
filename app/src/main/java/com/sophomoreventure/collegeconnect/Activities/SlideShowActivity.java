package com.sophomoreventure.collegeconnect.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sophomoreventure.collegeconnect.ClubListAtivity;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.CreateEventActivity;
import com.sophomoreventure.collegeconnect.CustomLayoutManager;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.HorizontalRecyclerAdapter;
import com.sophomoreventure.collegeconnect.ImageHandler;
import com.sophomoreventure.collegeconnect.MyEventsActivity;
import com.sophomoreventure.collegeconnect.MyEventsAdapter;
import com.sophomoreventure.collegeconnect.Network.ServiceClass;
import com.sophomoreventure.collegeconnect.NoticeBoardActivity;
import com.sophomoreventure.collegeconnect.OtherEventView;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.SparshEventListAtivity;
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

public class SlideShowActivity extends DrawerActivity implements
        ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final long POLL_FREQUENCY =  5000 ;//28800000;
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
    private RecyclerView horizonatalRV;
    private RecyclerView aRV;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_slide_show);
        //View child = getLayoutInflater().inflate(R.layout.main_layout, null);
        //LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View myView = inflater.inflate(R.layout.main_layout, null);


        FrameLayout activityContainer = (FrameLayout) findViewById(R.id.activity_content);
        getLayoutInflater().inflate(R.layout.main_layout, activityContainer, true);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
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
                    collapsingToolbarLayout.setTitle("College Connect");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });


        Resources res = this.getResources();
        int id = R.drawable.poster_five;
        Bitmap bmp = BitmapFactory.decodeResource(res, id);
        ImageHandler handler = new ImageHandler(this);
        handler.save(bmp, "poster");
        Log.i("vikas kumar", "written");

        //setupDrawer();
//        setupJob();
//        mainScreen = (LinearLayout) findViewById(R.id.main_screen);

        slideShowPager = (ViewPager) findViewById(R.id.slideShowPager);
        slideShowPager.setAdapter(new SlideShowAdapter(getSupportFragmentManager()));

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

        aRV.setAdapter(new MyEventsAdapter(SlideShowActivity.this, "",0));


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected boolean useToolbar() {
        return false;
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
        mNavView.setCheckedItem(R.id.nav_events);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle dt = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                0, 0
        );
        mDrawerLayout.setDrawerListener(dt);
        dt.syncState();

        View header = mNavView.getHeaderView(0);

        TextView userNameTextView = (TextView) header.findViewById(R.id.drawer_user_name);
        userNameTextView.setText(EventUtility.getUserNameFromPref(this));
        TextView userEmailTextView = (TextView) header.findViewById(R.id.drawer_user_email);
        userEmailTextView.setText(EventUtility.getUserEmailFromPref(this));



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


//    private void translateMainScreen(float slideOffset) {
//        if (mainScreen != null) {
//
//            mainScreen.setTranslationX(slideOffset * 550);
//        }
//    }


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

        if (id == R.id.create_event_activity) {

            startActivity(new Intent(this, CreateEventActivity.class));
        }

        if (id == R.id.event_view_activity) {

            startActivity(new Intent(this, OtherEventView.class));
        }

        if (id == R.id.my_event_activity) {

            startActivity(new Intent(this, MyEventsActivity.class));
        }
        if (id == R.id.logout) {
            SharedPreferences pref = getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onStart() {
        super.onStart();
        /*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SlideShow Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sophomoreventure.collegeconnect.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        */
    }

    @Override
    public void onStop() {
        super.onStop();
        /*
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SlideShow Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sophomoreventure.collegeconnect.Activities/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
        */
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
