package com.sophomoreventure.collegeconnect.Activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.R;

import java.util.ArrayList;

/**
 * Created by Murali on 23/01/2016.
 */
public class ClubListAtivity extends DrawerBaseActivity {
    RecyclerView clubListRV;
    String[] clubNameList;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
        getSupportActionBar().setTitle("College Clubs");
        clubNameList = getResources().getStringArray(R.array.club_list);
        clubListRV = (RecyclerView) findViewById(R.id.clubListRecyclerView);
        clubListRV.setLayoutManager(new LinearLayoutManager(this));
        clubListRV.setAdapter(new ClubListAdapter(this, clubNameList));
        clubListRV.setHasFixedSize(true);
        overridePendingTransition(0, android.R.anim.fade_out);

    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_COLLEGE_CLUBS;
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

    class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.ViewHolder> {

        Context context;
        String[] clubList;
        ClubsDataBase database;
       ArrayList<String> titles;

        public ClubListAdapter(Context context, String[] clubList) {
            this.context = context;

            this.clubList = clubList;
            database = new ClubsDataBase(context);
            titles = database.getClubTitles();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (parent instanceof RecyclerView) {
                int layoutId = R.layout.club_layout;
                View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                view.setFocusable(true);
                return new ViewHolder(view);
            } else {
                throw new RuntimeException("Not bound to RecyclerView");
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

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


            public ViewHolder(View itemView) {
                super(itemView);
//                clubImageView = (ImageView) itemView.findViewById(R.id.clubImageView);
                clubNameTextView = (TextView) itemView.findViewById(R.id.clubNameTextView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClubDetailActivity.class);
                intent.putExtra("position",getPosition());
                context.startActivity(intent);
            }
        }
    }

    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ClubListAtivity.this, activity));
            }
        }, 260);
    }

}
