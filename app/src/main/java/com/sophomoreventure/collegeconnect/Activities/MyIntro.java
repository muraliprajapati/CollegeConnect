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

        addSlide(AppIntroFragment.newInstance("Intro by vikas", "this is so cool app",
                R.drawable.poster_five, Color.parseColor("#FFA1C8F1")));
        addSlide(AppIntroFragment.newInstance("Intro continued", "this is it",
                R.drawable.poster_five, Color.parseColor("#FFA1C8F1")));

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
