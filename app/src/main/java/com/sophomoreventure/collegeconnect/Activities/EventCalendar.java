package com.sophomoreventure.collegeconnect.Activities;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.sophomoreventure.collegeconnect.R;

import java.util.Calendar;
import java.util.List;

public class EventCalendar extends AppCompatActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    private WeekView mWeekView;
    private WeekView.EventClickListener mEventClickListener;
    private MonthLoader.MonthChangeListener mMonthChangeListener;
    private WeekView.EventLongPressListener mEventLongPressListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        //mWeekView.setOnEventClickListener(this);


        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
       // mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
       // mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
       // mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }
}
