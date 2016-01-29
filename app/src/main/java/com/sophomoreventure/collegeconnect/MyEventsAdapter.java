package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;

import java.util.ArrayList;

/**
 * Created by Murali on 10/01/2016.
 */
public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {

    Context context;
    String clubName;
    EventDatabase eventDatabase;
    ArrayList<Event> listData;
    private BlurLayout mSampleLayout;
    View hover;

    public MyEventsAdapter(Context context, String clubName, BlurLayout sampleLayout, View hover) {
        this.context = context;
        this.clubName = clubName;
        mSampleLayout = sampleLayout;
        this.hover = hover;
        eventDatabase = new EventDatabase(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            int layoutId = R.layout.event_card_view;
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            mSampleLayout = (BlurLayout) view.findViewById(R.id.blur_layout);
            hover = LayoutInflater.from(context).inflate(R.layout.hover_view, null);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        listData = eventDatabase.selectByClub(clubName);
        if (listData.size() != 0) {
            holder.eventNameTextView.setText(listData.get(position).getEventTitle());
            holder.eventClubTextView.setText(listData.get(position).getEventClub());
           // holder.dateTextView.setText((int) listData.get(position).getEventStarttime());
        }

    }


    @Override
    public int getItemCount() {

        return 6;
    }

    public void showHover(){

        BlurLayout.setGlobalDefaultDuration(450);

        hover.findViewById(R.id.heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(v);
            }
        });
        hover.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Swing)
                        .duration(550)
                        .playOn(v);
            }
        });
        mSampleLayout.setHoverView(hover);
        mSampleLayout.setBlurDuration(550);

        mSampleLayout.addChildAppearAnimator(hover, R.id.heart, Techniques.FlipInX, 550, 0);
        mSampleLayout.addChildAppearAnimator(hover, R.id.share, Techniques.FlipInX, 550, 250);
        mSampleLayout.addChildAppearAnimator(hover, R.id.more, Techniques.FlipInX, 550, 500);

        mSampleLayout.addChildDisappearAnimator(hover, R.id.heart, Techniques.FlipOutX, 550, 500);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.share, Techniques.FlipOutX, 550, 250);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.more, Techniques.FlipOutX, 550, 0);

        mSampleLayout.addChildAppearAnimator(hover, R.id.description, Techniques.FadeInUp);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.description, Techniques.FadeOutDown);
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
            //dateTextView = (TextView) itemView.findViewById(R.id.eventDateTextView);
//            attendingCheckBox = (CheckBox) itemView.findViewById(R.id.attendingCheckBox);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, EventView.class);
            intent.putExtra("clubName", clubName);
            intent.putExtra("position", getPosition());
            context.startActivity(intent);
            //showHover();
        }
    }
}
