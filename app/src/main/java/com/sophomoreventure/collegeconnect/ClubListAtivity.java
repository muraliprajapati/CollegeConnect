package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
            super.onBackPressed();
        }
    }

    class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.ViewHolder> {

        Context context;
        String[] clubList;
        int[] clubImageList;

        public ClubListAdapter(Context context, String[] clubList, int[] clubImageList) {
            this.context = context;
            this.clubImageList = clubImageList;
            this.clubList = clubList;
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
            holder.clubNameTextView.setText(clubList[position]);
//            holder.clubImageView.setImageResource(clubImageList[position]);
        }


        @Override
        public int getItemCount() {

            return 5;
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
                context.startActivity(intent);
            }
        }
    }
}
