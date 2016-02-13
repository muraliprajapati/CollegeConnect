package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.sophomoreventure.collegeconnect.fragments.AppIntroFragment;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Murali on 13/02/2016.
 */
public class NewAppIntro extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager slideShowPager;
    ImageButton button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_show);
        slideShowPager = (ViewPager) findViewById(R.id.slideShowPager);
        slideShowPager.setAdapter(new SlideShowAdapter(getSupportFragmentManager(), this));
        slideShowPager.addOnPageChangeListener(this);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(slideShowPager);
        button = (ImageButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAppIntro.this, SplashActivity.class));
                finish();
            }
        });
        overridePendingTransition(0, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 3) {
            button.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class SlideShowAdapter extends FragmentStatePagerAdapter {
        Context context;


        public SlideShowAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;


        }

        @Override
        public Fragment getItem(int position) {

            return AppIntroFragment.newInstance(position, context);
        }

        @Override
        public int getCount() {
            return 4;

        }
    }

}
