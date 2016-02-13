package com.sophomoreventure.collegeconnect.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sophomoreventure.collegeconnect.R;

/**
 * Created by Murali on 13/02/2016.
 */
public class AppIntroFragment extends Fragment {

    int imageRes[] = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four};

    public static Fragment newInstance(int position, Context context) {
        Bundle bundle = new Bundle();
        AppIntroFragment fragment = new AppIntroFragment();
        bundle.putInt("ResIdPosition", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_intro, container, false);
        ImageView slideShowImage = (ImageView) view.findViewById(R.id.slideShowImageView);
        slideShowImage.setImageResource(imageRes[getArguments().getInt("ResIdPosition")]);
        return view;
    }
}
