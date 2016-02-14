package com.sophomoreventure.collegeconnect.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sophomoreventure.collegeconnect.Event;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Murali on 08/12/2015.
 */
public class SlideShowFragment extends Fragment {
    public static final String IMAGE_RES_KEY = "res_id";
    static int imageResId = 0;
    static ImageLoader mImageLoader;
    static VolleySingleton mVolleySingleton;
    static EventDatabase database;
    static ArrayList<Event> listData;
    static int Position;
    private WeakReference<ImageView> imageViewReference;

    public static Fragment newInstance(int position,Context context) {
        Bundle bundle = new Bundle();
        SlideShowFragment fragment = new SlideShowFragment();
        fragment.setArguments(bundle);
        database = new EventDatabase(context);
        listData = database.viewSlideShowData();
        mVolleySingleton = VolleySingleton.getInstance(context);
        mImageLoader = mVolleySingleton.getImageLoader();
        Position = position;
        return fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow,container,false);
        ImageView slideShowImage = (ImageView) view.findViewById(R.id.slideShowImageView);
        imageViewReference = new WeakReference<ImageView>(slideShowImage);

        if(listData != null){
            if(listData.size() != 0){
                if(listData.size() == 1){
                    loadImages(listData.get(0).getUrlThumbnail(),slideShowImage);
                }else{
                    if(Position < listData.size()){
                        loadImages(listData.get(Position).getUrlThumbnail(),slideShowImage);
                    }
                }
            }
        }
        //Bitmap bitmap = decodeSampledBitmapFromResource(getResources(),
      //          getArguments().getInt(IMAGE_RES_KEY, R.drawable.poster_four), 300, 200);
       // if (imageViewReference != null && bitmap != null) {
       //     if (imageViewReference.get() != null) {
       //         imageViewReference.get().setImageBitmap(bitmap);
       //     }
       // }
//        slideShowImage.setImageBitmap();

        return view;
    }


    private void loadImages(String urlThumbnail, final ImageView holder) {
        if (true) {
            mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (imageViewReference != null && response.getBitmap() != null) {
                        if (imageViewReference.get() != null) {
                            imageViewReference.get().setImageBitmap(response.getBitmap());
                        }
                    }
                }
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }


}
