package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Murali on 10/01/2016.
 */
public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    Context context;
    String[] clubList = {"Sparsh", "CHRD", "CHRD", "Sparsh", "Sparsh", "Drishti"};
    String[] eventList = {"Sparsh", "Shamiyana", "Chhanak", "Fashion Night", "Singing Night", "Udaan"};
    int[] imageResArray = new int[]{R.drawable.poster_one, R.drawable.poster_two, R.drawable.poster_three, R.drawable.poster_four, R.drawable.poster_five, R.drawable.poster_six};
    String clubName;
    EventDatabase eventDatabase;
    ArrayList<Event> listData;


    public MyEventsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            int layoutId = R.layout.event_card_view;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eventImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(),
                imageResArray[position], 300, 200));
        holder.eventNameTextView.setText(eventList[position]);
        holder.eventClubTextView.setText(clubList[position]);

    }


    @Override
    public int getItemCount() {

        return 6;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView eventImageView;
        TextView eventNameTextView;
        TextView eventClubTextView;
        TextView dateTextView;
        CheckBox attendingCheckBox;


        public ViewHolder(View itemView) {
            super(itemView);
            eventImageView = (ImageView) itemView.findViewById(R.id.eventImageView);
            eventNameTextView = (TextView) itemView.findViewById(R.id.eventNameTextView);
            eventClubTextView = (TextView) itemView.findViewById(R.id.eventClubTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.eventDateTextView);
//            attendingCheckBox = (CheckBox) itemView.findViewById(R.id.attendingCheckBox);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(context, EventView.class);
//            context.startActivity(intent);
        }
    }
}
