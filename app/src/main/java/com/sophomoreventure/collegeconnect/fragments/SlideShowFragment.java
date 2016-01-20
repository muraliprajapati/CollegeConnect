package com.sophomoreventure.collegeconnect.fragments;

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

import com.sophomoreventure.collegeconnect.R;

/**
 * Created by Murali on 08/12/2015.
 */
public class SlideShowFragment extends Fragment {
    public static final String IMAGE_RES_KEY = "res_id";
    static int imageResId = 0;

    public static Fragment newInstance(int id) {
        Bundle bundle = new Bundle();
        SlideShowFragment fragment = new SlideShowFragment();
        bundle.putInt(IMAGE_RES_KEY, id);
        fragment.setArguments(bundle);
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
        slideShowImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                getArguments().getInt(IMAGE_RES_KEY, R.drawable.poster_four), 300, 200));

        return view;
    }
}
