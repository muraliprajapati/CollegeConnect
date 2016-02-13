package com.sophomoreventure.collegeconnect.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.sophomoreventure.collegeconnect.BaseActivity;
import com.sophomoreventure.collegeconnect.ModelClass.ClubModel;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.SparshEventActivity;

import java.util.ArrayList;

/**
 * Created by Murali on 23/01/2016.
 */
public class ClubDetailActivity extends BaseActivity implements ObservableScrollViewCallbacks {
    TextView titleTextView,mClubDescription,mOrganizerName,mOrganizerMob,mOrganizerMobTwo,mOrganizerNameTwo,mClubName;
    ClubsDataBase database;
    ArrayList<String> titles;
    ImageView imageView;
    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private VolleySingleton mVolleySingleton;
    private ImageLoader mImageLoader;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_detail_layout);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        mVolleySingleton = VolleySingleton.getInstance(this);
        mImageLoader = mVolleySingleton.getImageLoader();

        position = getIntent().getIntExtra("position", 0);
        database = new ClubsDataBase(this);
        titles = database.getClubTitles();

        mImageView = findViewById(R.id.club_image);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setTextColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorWhite)));
        mClubDescription = (TextView) findViewById(R.id.club_description);
        mOrganizerName = (TextView) findViewById(R.id.event_organizer_name);
        mOrganizerMob = (TextView) findViewById(R.id.event_organizer_phone);
        mOrganizerMobTwo = (TextView) findViewById(R.id.tv4);
        mOrganizerNameTwo = (TextView) findViewById(R.id.tv3);
        imageView = (ImageView) findViewById(R.id.club_image);
        mClubName = (TextView) findViewById(R.id.club_name);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        setData(position,this);

        Button button = (Button) findViewById(R.id.eventsByClubButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClubDetailActivity.this, SparshEventActivity.class);
                i.putExtra("clubName",mClubName.getText().toString());
                i.putExtra("position",position);
                startActivity(i);
            }
        });
    }


    public void setData(int position,Context context){
        ClubModel club = database.viewAllData(titles.get(position));
        titleTextView.setText(club.getClubName());
//        getSupportActionBar().setTitle(club.getClubName());
        mClubName.setText(club.getClubName());
        mClubDescription.setText(club.getClubDescription());

        String clubHeadName = club.getClubHead();
        String clubHeadMob = club.getClubHeadMob();

        //Log.i("expose", club.getClubName().toString() + clubHeadName + " " + " " + clubHeadMob + clubHeadName.lastIndexOf("?"));


        if(clubHeadName.toString().length() > 1 && clubHeadMob.toString().length() > 1){
            mOrganizerName.setText(clubHeadName.substring(1));
            mOrganizerMob.setText(clubHeadMob.substring(1));
        }else {
            mOrganizerName.setText("N/A");
            mOrganizerMob.setText("N/A");
        }



        if(clubHeadName.lastIndexOf("?") != -1 && clubHeadName.lastIndexOf("?") != clubHeadName.indexOf("?")){
            mOrganizerName.setText(clubHeadName.substring(1, clubHeadName.lastIndexOf("?") - 1));
            mOrganizerMob.setText(clubHeadMob.substring(1, clubHeadMob.lastIndexOf("?") - 1));
        }

        mOrganizerMobTwo.setText("None");
        mOrganizerNameTwo.setText("None");

        if(clubHeadName.lastIndexOf("?") != -1 && clubHeadName.lastIndexOf("?") != clubHeadName.indexOf("?")){
            mOrganizerNameTwo.setText(clubHeadName.substring(clubHeadName.lastIndexOf("?") + 1));
            mOrganizerMobTwo.setText(clubHeadMob.substring(clubHeadMob.lastIndexOf("?") + 1));
        }else {
            mOrganizerNameTwo.setText("N/A");
            mOrganizerMobTwo.setText("N/A");
        }



        loadImages(club.getImageUrl(),imageView);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        titleTextView.setTextColor(ScrollUtils.getColorWithAlpha(alpha, getResources().getColor(R.color.colorWhite)));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    private void loadImages(String urlThumbnail, final ImageView holder) {
        if (true) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                    holder.setImageBitmap(response.getBitmap());

                }
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                Intent i = new Intent(ClubDetailActivity.this, ClubListAtivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
