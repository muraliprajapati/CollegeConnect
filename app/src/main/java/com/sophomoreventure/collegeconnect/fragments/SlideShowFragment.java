package com.sophomoreventure.collegeconnect.fragments;

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




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slideshow,container,false);
        ImageView slideShowImage = (ImageView) view.findViewById(R.id.slideShowImageView);
        slideShowImage.setImageResource(getArguments().getInt(IMAGE_RES_KEY,R.drawable.pixeldropr));
        return view;
    }
}
