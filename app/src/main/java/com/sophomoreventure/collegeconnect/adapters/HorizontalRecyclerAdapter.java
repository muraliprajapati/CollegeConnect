package com.sophomoreventure.collegeconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.SparshEventActivity;

import java.util.ArrayList;

/**
 * Created by Murali on 10/01/2016.
 */
public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.ViewHolder> {

    Context context;
    String clubName;
    ArrayList<String> listClubs;
    ClubsDataBase database;
    EventDatabase eventDatabase;
    ArrayList<Event> events;

    public HorizontalRecyclerAdapter(Context context) {
        this.context = context;
        database = new ClubsDataBase(context);
        eventDatabase = new EventDatabase(context);
        listClubs = database.getClubTitlesNormal();
    }

    @Override
    public HorizontalRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(listClubs != null ){
            if (listClubs.size() != 0){
                if (parent instanceof RecyclerView) {
                    int layoutId = R.layout.single_column;
                    View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                    view.setFocusable(true);
                    return new ViewHolder(view);
                } else {
                    throw new RuntimeException("Not bound to RecyclerView");
                }
            }else {
                int layoutId = R.layout.message_view;
                View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                view.setFocusable(true);
                return new ViewHolder(view);
            }
        }else {
            int layoutId = R.layout.message_view;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(HorizontalRecyclerAdapter.ViewHolder holder, int position) {

        if(listClubs != null){
            if(listClubs.size() != 0){
                holder.clubNameTextView.setText(listClubs.get(position));
            }
        }

    }


    @Override
    public int getItemCount() {
        if(listClubs != null){
            if (listClubs.size() != 0){
                return listClubs.size();
            }else {
                return 1;
            }
        }else {
            return 1;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout layout;
        TextView clubNameTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            clubNameTextView = (TextView) itemView.findViewById(R.id.club_name_text_view);
            layout = (LinearLayout) itemView.findViewById(R.id.single_column_linear_layout);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), SparshEventActivity.class);
            i.putExtra("position", getPosition());
            if(clubNameTextView != null){
                i.putExtra("clubName",clubNameTextView.getText().toString());
                context.startActivity(i);
            }


        }
    }
}
