package com.sophomoreventure.collegeconnect.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sophomoreventure.collegeconnect.Activities.LoginActivity;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.fragments.FragmentDrawer;
import com.sophomoreventure.collegeconnect.fragments.SlideShowFragment;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Murali on 08/12/2015.
 */
public class SlideShowActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    ViewPager slideShowPager;
    SlideShowAdapter adapter;
    Timer timer;
    Toolbar toolbar;
    int currentPage = 0;
    int[] imageResArray = new int[]{R.drawable.pixeldropr, R.drawable.bird, R.drawable.pixel};
    //a layout grouping the toolbar and the tabs together
    //private ViewGroup mContainerToolbar;
    private FragmentDrawer mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
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


        return super.onOptionsItemSelected(item);
    }

    /*
    public View getContainerToolbar() {
        return mContainerToolbar;
    }
    */

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


