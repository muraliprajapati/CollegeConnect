package com.sophomoreventure.collegeconnect;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
    //private Toolbar mToolbar;
    //a layout grouping the toolbar and the tabs together
    //private ViewGroup mContainerToolbar;
    private FragmentDrawer mDrawerFragment;


    int currentPage = 0;
    int[] imageResArray = new int[]{R.drawable.pixeldropr, R.drawable.bird, R.drawable.pixel};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

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
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));//, mToolbar);

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


