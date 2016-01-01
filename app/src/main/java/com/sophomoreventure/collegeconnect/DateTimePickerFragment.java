package com.sophomoreventure.collegeconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Murali on 24/12/2015.
 */
public class DateTimePickerFragment extends DialogFragment implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private static final String KEY_EVENT_ID = "event_id";
    public static String KEY_INDEX = "index";
    public static String EXTRA_EVENT_ID = "event_id";
    Button positiveButton;
    Button negativeButton;
    View view;
    AlertDialog alertDialog;

    DatePicker datePicker;
    TimePicker timePicker;
    UUID eventId;

    EventHub eventHub;
    Event event;

    int index;

    int dayOfMonth;
    int monthOfYear;
    int year;
    int hour;
    int minute;

    static DateTimePickerFragment newInstance(String id, int index) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_ID, id);
        bundle.putInt(KEY_INDEX, index);
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("tag", "date-onCreaateDialog");
        index = getArguments().getInt(KEY_INDEX);
        eventId = UUID.fromString(getArguments().getString(KEY_EVENT_ID));

        view = View.inflate(getActivity(), R.layout.fragment_date_time_picker, null);
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Set Time For Notification");

        datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        positiveButton = (Button) view.findViewById(R.id.positive_button);
        negativeButton = (Button) view.findViewById(R.id.negative_button);

        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);


        eventHub = EventHub.getEventHub(getActivity());
        event = eventHub.getEventForId(eventId);

        alertDialog.setView(view);
        return alertDialog;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.positive_button:
                dayOfMonth = datePicker.getDayOfMonth();
                monthOfYear = datePicker.getMonth();
                year = datePicker.getYear();
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();

                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth, hour, minute);
                event.addNotificationDateTime(index, calendar);
                sendResult(Activity.RESULT_OK, event.getEventId());

                Log.i("tag", "" + datePicker.getDayOfMonth());
                Log.i("tag", "" + datePicker.getMonth());
                Log.i("tag", "" + datePicker.getYear());
                Log.i("tag", "" + timePicker.getCurrentHour());
                Log.i("tag", "" + timePicker.getCurrentMinute());

                alertDialog.dismiss();
                break;

            case R.id.negative_button:
                sendResult(Activity.RESULT_CANCELED, eventId);
                alertDialog.dismiss();
                break;
        }

    }

    void sendResult(int resultCode, UUID id) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_EVENT_ID, id.toString());
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


    @Override
    public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar();
        int presentYear = calendar.get(Calendar.YEAR);
        int presentMonth = calendar.get(Calendar.MONTH);
        int presentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (year < presentYear || monthOfYear < presentMonth || dayOfMonth < presentDay) {
            datePicker.init(presentYear, presentMonth, presentDay, this);
            Toast.makeText(getActivity(), "Time doesn't go backward", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("tag", "date-onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("tag", "date-onResume");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("tag", "date-onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("tag", "date-onDestroy");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("tag", "date-onDestroy");
    }
}