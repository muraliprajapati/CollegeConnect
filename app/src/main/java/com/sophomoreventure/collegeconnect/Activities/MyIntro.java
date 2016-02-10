package com.sophomoreventure.collegeconnect.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.SplashActivity;

/**
 * Created by Vikas Kumar on 08-02-2016.
 */
public class MyIntro extends AppIntro2 {


    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(AppIntroFragment.newInstance("College Connect", "Get Started",
                R.drawable.image_intro_one, Color.parseColor("#FF9800")));
        addSlide(AppIntroFragment.newInstance("College Connect", "Upcoming Events",
                R.drawable.image_intro_two, Color.parseColor("#FF9800")));
        addSlide(AppIntroFragment.newInstance("College Connect", "Create Event",
                R.drawable.image_intro_three, Color.parseColor("#FF9800")));
        addSlide(AppIntroFragment.newInstance("College Connect", "Know Your Clubs",
                R.drawable.image_intro_four, Color.parseColor("#FF9800")));

        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        showDoneButton(true);
        setProgressButtonEnabled(true);

        setVibrate(true);
        setVibrateIntensity(30);

    }



    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged() {

    }
}
