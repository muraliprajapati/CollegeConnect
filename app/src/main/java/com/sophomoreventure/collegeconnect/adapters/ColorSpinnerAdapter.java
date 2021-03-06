package com.sophomoreventure.collegeconnect.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sophomoreventure.collegeconnect.R;

/**
 * Created by Murali on 31/01/2016.
 */
public class ColorSpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] list;
    String[] colorList = {"#FFFFFF", "#2196f3", "#9c27b0", "#607d8b", "#009688"};


    public ColorSpinnerAdapter(Context context, int resource, String[] list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.single_spinner_item, parent, false);

        }
        TextView colorView = (TextView) convertView.findViewById(R.id.colorHolder);
        TextView colorTextView = (TextView) convertView.findViewById(R.id.colorName);

        GradientDrawable sd = (GradientDrawable) colorView.getBackground();

        if (position == 0) {
            colorView.setVisibility(View.GONE);
            colorTextView.setText(list[position]);
        }
        sd.setColor(Color.parseColor(colorList[position]));
        colorTextView.setText(list[position]);
        return convertView;
    }
}
