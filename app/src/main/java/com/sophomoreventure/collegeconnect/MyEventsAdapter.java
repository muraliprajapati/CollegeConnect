package com.sophomoreventure.collegeconnect;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sophomoreventure.collegeconnect.ModelClass.ClubsDataBase;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.SqlDataListener;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import java.lang.ref.WeakReference;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Murali on 10/01/2016.
 */
public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> implements SqlDataListener {

    private final int mPosition;
    Context context;
    String clubName;
    EventDatabase eventDatabase;
    ArrayList<String> listClubs;
    ClubsDataBase database;
    ArrayList<Event> listData;
    private ImageLoader mImageLoader;
    private VolleySingleton mVolleySingleton;
    private WeakReference<ImageView> imageViewReference;
    private ArrayList<String> likedEventList;



    public MyEventsAdapter(Context context, String clubName,int position) {
        this.context = context;
        this.clubName = clubName;
        mPosition = position;
        database = new ClubsDataBase(context);
        eventDatabase = new EventDatabase(context);
        mVolleySingleton = new VolleySingleton(context);
        mImageLoader = mVolleySingleton.getImageLoader();
        likedEventList = null;

        listClubs = database.getClubTitles();
        Log.i("vikas",listClubs.size() + "");

        if(clubName.equals("SlideShowView")){
            listData = eventDatabase.viewAllData();
        }else{
            if(listClubs != null){
                if(listClubs.size() != 0){

                    listData = eventDatabase.selectByClub(clubName);
                    //eventDatabase.selectByClubName(context,clubName);
                }
            }
        }
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if(listClubs != null && listData != null){
            if (parent instanceof RecyclerView) {
                layoutId = R.layout.event_card_view;
            } else {
                throw new RuntimeException("Not bound to RecyclerView");
            }
        }else {
            layoutId = R.layout.message_view;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Bitmap bitmap = decodeSampledBitmapFromResource(context.getResources(),
          //      imageResArray[position], 300, 200);

        if(listData != null){
            if (listData.size() != 0) {

                Date date = new java.util.Date(Long.parseLong(String.valueOf(listData.get(position).getEventStarttime())));
                String eventDateTime = new SimpleDateFormat("MMMM,dd,yyyy HH:mm a").format(date);
                holder.eventNameTextView.setText(listData.get(position).getEventTitle());
                holder.eventClubTextView.setText(listData.get(position).getEventClub());
                holder.dateTextView.setText(eventDateTime);

                if(likedEventList != null){
                    if(likedEventList.size() != 0){
                        for(int i = 0;i<likedEventList.size();i++){

                            if(likedEventList.get(i) == listData.get(position).getEventServerId()){

                                holder.attendingCheckBox.setChecked(true);
                            }

                        }

                    }
                }
                String urlThumnail = listData.get(position).getUrlThumbnail();
                loadImages(urlThumnail, holder);

            }else {
                holder.wrongTextView.setText("No Event by " + clubName+ " Yet");
                    holder.eventImageView.setVisibility(View.GONE);
                holder.wrongTextView.setVisibility(View.VISIBLE);
                holder.container.setVisibility(View.GONE);
                holder.eventClubTextView.setVisibility(View.GONE);
            }
        }else {
            holder.wrongTextView.setText("No Event by " + clubName+ " Yet");
            holder.container.setVisibility(View.GONE);
            holder.eventClubTextView.setVisibility(View.GONE);
            holder.eventImageView.setVisibility(View.GONE);
            holder.wrongTextView.setVisibility(View.VISIBLE);

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

    private void loadImages(String urlThumbnail, final ViewHolder holder) {
        if (true) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.eventImageView.setImageBitmap(response.getBitmap());
                }
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public void loadData(ArrayList<Event> data) {
        listData = data;
        notifyItemInserted(listData.size());
    }

    @Override
    public void loadEventById(Event event) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView eventImageView;
        TextView eventNameTextView;
        TextView eventClubTextView;
        TextView dateTextView;
        CheckBox attendingCheckBox;
        TextView wrongTextView;
        LinearLayout container;


        public ViewHolder(View itemView) {
            super(itemView);
            eventImageView = (ImageView) itemView.findViewById(R.id.eventImageView);
            eventNameTextView = (TextView) itemView.findViewById(R.id.eventNameTextView);
            eventClubTextView = (TextView) itemView.findViewById(R.id.eventClubTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.eventDateTextView);
            imageViewReference = new WeakReference<ImageView>(eventImageView);
            attendingCheckBox = (CheckBox) itemView.findViewById(R.id.attendingCheckBox);
            wrongTextView = (TextView) itemView.findViewById(R.id.nodata);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            if(attendingCheckBox != null){
                attendingCheckBox.setOnClickListener(this);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view.getId() == attendingCheckBox.getId()){
                Event event = listData.get(getPosition());
                String id = event.getEventServerId();
                boolean attending = attendingCheckBox.isChecked();
                try {
                    sendAttendRequest(id, attending);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                if(attending){
                    attendingCheckBox.setChecked(true);
                }else {
                    attendingCheckBox.setChecked(false);
                }

            }else {

                if(listData != null){
                    if (listData.size() != 0) {
                        Intent intent = new Intent(context, OtherEventView.class);
                        intent.putExtra("clubName", clubName);
                        intent.putExtra("eventId",listData.get(getPosition()).getEventServerId());
                        intent.putExtra("position", getPosition());
                        context.startActivity(intent);
                    }
                }

            }
        }

        private void sendAttendRequest(String eventID, boolean attend) throws NoSuchAlgorithmException, KeyManagementException {
            VolleySingleton volleySingleton =  new VolleySingleton(context);
            RequestQueue requestQueue = volleySingleton.getRequestQueue();
            if(attend){
                RequestorPost.attendRequest(requestQueue, API.FOLLOW_EVENT_API + eventID + "/follow", context);
            }else {
                RequestorPost.attendRequest(requestQueue,API.FOLLOW_EVENT_API+eventID+"/unfollow",context);
            }

        }
    }
}
