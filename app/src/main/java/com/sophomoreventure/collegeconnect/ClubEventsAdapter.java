package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Murali on 10/01/2016.
 */
public class ClubEventsAdapter extends RecyclerView.Adapter<ClubEventsAdapter.ViewHolder> {

    Context context;
    int[] imageResArray = new int[]{R.drawable.poster_six};
    String clubName;
    EventDatabase eventDatabase;
    ArrayList<String> listClubs;
    ClubsDataBase database;
    ArrayList<Event> listData;
    Event event;
    private WeakReference<ImageView> imageViewReference;


    public ClubEventsAdapter(Context context,String id) {
        this.context = context;
        eventDatabase = new EventDatabase(context);
        listData = eventDatabase.selectByClub(id);

    }

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

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
            if (parent instanceof RecyclerView) {
                layoutId = R.layout.event_card_view;
            } else {
                throw new RuntimeException("Not bound to RecyclerView");
            }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.eventImageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(),
                imageResArray[position], 300, 200));

        if ( listData != null) {

            if(listData.size() != 0){
                Date date = new java.util.Date(Long.parseLong(String.valueOf(listData.get(position).getEventStarttime())));
                String eventDateTime = new SimpleDateFormat("MMMM,dd,yyyy HH:mm a").format(date);

                holder.eventNameTextView.setText(listData.get(position).getEventTitle());
                holder.eventClubTextView.setText(listData.get(position).getEventClub());
                holder.dateTextView.setText(eventDateTime);
                if(listData.get(0).getEventLiked().equals("true")){
                    holder.attendingCheckBox.setChecked(true);
                }
            }else {

            }

        }else {


        }
//        if (listData.size() != 0) {
//            holder.eventNameTextView.setText(listData.get(position).getEventTitle());
//            holder.eventClubTextView.setText(listData.get(position).getEventClub());
//           // holder.dateTextView.setText((int) listData.get(position).getEventStarttime());
//        }

    }

    @Override
    public int getItemCount() {

        if(listData != null){
            if (listData.size() != 0) {
                return listData.size();
            }
            else {
                return 1;
            }

        }
        else return 1;
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
            imageViewReference = new WeakReference<ImageView>(eventImageView);
            //dateTextView = (TextView) itemView.findViewById(R.id.eventDateTextView);
            attendingCheckBox = (CheckBox) itemView.findViewById(R.id.attendingCheckBox);
            itemView.setOnClickListener(this);;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, EventView.class);
            if(listData != null){
                if(listData.size() != 0){

                    if(listData.get(getPosition()).getEventServerId() != null){
                        intent.putExtra("clubId", listData.get(getPosition()).getEventServerId());
                    }

                }
            }

            context.startActivity(intent);
        }
    }

}
