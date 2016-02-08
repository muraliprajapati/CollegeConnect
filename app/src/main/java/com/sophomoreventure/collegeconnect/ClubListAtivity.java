package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sophomoreventure.collegeconnect.Activities.DrawerActivity;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by Murali on 23/01/2016.
 */
public class ClubListAtivity extends DrawerBaseActivity {
    RecyclerView clubListRV;
    int[] imageResArray = new int[]{R.drawable.poster_five, R.drawable.poster_four, R.drawable.poster_three, R.drawable.poster_two, R.drawable.poster_three};
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
        clubListRV.setAdapter(new ClubListAdapter(this, clubNameList, imageResArray));
        clubListRV.setHasFixedSize(true);

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
            super.onBackPressed();
        }
    }

    class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.ViewHolder> {

        Context context;
        String[] clubList;
        int[] clubImageList;
        ClubsDataBase database;
       ArrayList<String> titles;

        public ClubListAdapter(Context context, String[] clubList, int[] clubImageList) {
            this.context = context;
            this.clubImageList = clubImageList;
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
}
