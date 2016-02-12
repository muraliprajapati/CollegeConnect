package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.Activities.DrawerBaseActivity;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;

import java.util.ArrayList;

/**
 * Created by Murali on 23/01/2016.
 */
public class SparshEventListAtivity extends DrawerBaseActivity {
    RecyclerView clubListRV;
    String[] sparshEventList = {"Technical", "Managerial", "Informal", "Lectures & Exhibitions", "Mega Attraction"};
    private DrawerLayout mDrawerLayout;

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 1;
            final int halfWidth = width / 1;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setTitle("Sparsh Events");
        clubListRV = (RecyclerView) findViewById(R.id.clubListRecyclerView);
        clubListRV.setLayoutManager(new LinearLayoutManager(this));
        clubListRV.setAdapter(new SparshEventListAdapter(this, sparshEventList));
        clubListRV.setHasFixedSize(true);
        overridePendingTransition(0, 0);

    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_SPARSH_EVENTS;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    class SparshEventListAdapter extends RecyclerView.Adapter<SparshEventListAdapter.ViewHolder> {

        Context context;
        String[] clubList;
        ClubsDataBase database;
        ArrayList<String> titles;


        public SparshEventListAdapter(Context context, String[] clubList) {
            this.context = context;
            this.clubList = clubList;
            database = new ClubsDataBase(context);
            titles = database.getClubTitlesSparsh();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (parent instanceof RecyclerView) {
                int layoutId = R.layout.sparsh_event_layout;
                View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                view.setFocusable(true);
                return new ViewHolder(view);
            } else {
                throw new RuntimeException("Not bound to RecyclerView");
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
              //  holder.layout.setBackground(new BitmapDrawable(decodeSampledBitmapFromResource(getResources(), clubImageList[position], 300, 150)));
          //  } else {
           //     holder.layout.setBackgroundResource(clubImageList[position]);
           // }

            if(titles != null){
                if(titles.size() != 0){

                    holder.clubNameTextView.setText(titles.get(position));

                }else {
                    holder.clubNameTextView.setText("No Data Available");
                }
            }else {
                holder.clubNameTextView.setText("No Data Available");
            }


//            holder.clubImageView.setImageResource(clubImageList[position]);
        }


        @Override
        public int getItemCount() {

            if(titles != null){
                if(titles.size() != 0){
                    return titles.size();
                }else {return 1;}
            }else {
                return 1;
            }
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView clubImageView;
            TextView clubNameTextView;
            RelativeLayout layout;


            public ViewHolder(View itemView) {
                super(itemView);
//                clubImageView = (ImageView) itemView.findViewById(R.id.clubImageView);
                clubNameTextView = (TextView) itemView.findViewById(R.id.sparshEventTextView);
                layout = (RelativeLayout) itemView.findViewById(R.id.sparsh_back);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SparshEventActivity.class);
                if(titles.size() !=  0){
                    intent.putExtra("clubName",titles.get(getPosition()));
                    intent.putExtra("position", getPosition());
                    context.startActivity(intent);
                }

            }
        }
    }

    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SparshEventListAtivity.this, activity));
            }
        }, 260);
    }
}
