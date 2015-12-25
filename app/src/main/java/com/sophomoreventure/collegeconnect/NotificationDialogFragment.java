package com.sophomoreventure.collegeconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Murali on 24/12/2015.
 */
public class NotificationDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int FIRST_REQUEST_CODE = 1;
    private static final int SECOND_REQUEST_CODE = 2;
    private static final int THIRD_REQUEST_CODE = 3;
    private static final int FOURTH_REQUEST_CODE = 4;
    private static final int FIFTH_REQUEST_CODE = 5;

    private static final String KEY_EVENT_ID = "event_id";

    UUID eventId;

    Button firstPickDateButton;
    Button secondPickDateButton;
    Button thirdPickDateButton;
    Button fourthPickDateButton;
    Button fifthPickDateButton;

    TextView firstNotificationTextView;
    TextView secondNotificationTextView;
    TextView thirdNotificationTextView;
    TextView fourthNotificationTextView;
    TextView fifthNotificationTextView;

    EventHub eventHub;
    Event event;

    public static NotificationDialogFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_ID, id);
        NotificationDialogFragment fragment = new NotificationDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        eventId = UUID.fromString(getArguments().getString(KEY_EVENT_ID));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification_dialog, null, false);

        eventHub = EventHub.getEventHub(getActivity());
        event = eventHub.getEventForId(eventId);

        firstNotificationTextView = (TextView) view.findViewById(R.id.firstNotification);
        secondNotificationTextView = (TextView) view.findViewById(R.id.secondNotification);
        thirdNotificationTextView = (TextView) view.findViewById(R.id.thirdNotification);
        fourthNotificationTextView = (TextView) view.findViewById(R.id.fourthNotification);
        fifthNotificationTextView = (TextView) view.findViewById(R.id.fifthNotification);

        if (!EventUtility.isEmpty(event.getNotificationDateTimeArray())) {
            firstNotificationTextView.setText(event.getNotificationDateTime(0));
            secondNotificationTextView.setText(event.getNotificationDateTime(1));
            thirdNotificationTextView.setText(event.getNotificationDateTime(2));
            fourthNotificationTextView.setText(event.getNotificationDateTime(3));
            fifthNotificationTextView.setText(event.getNotificationDateTime(4));
        }

        firstPickDateButton = (Button) view.findViewById(R.id.firstPickDateButton);
        secondPickDateButton = (Button) view.findViewById(R.id.secondPickDateButton);
        thirdPickDateButton = (Button) view.findViewById(R.id.thirdPickDateButton);
        fourthPickDateButton = (Button) view.findViewById(R.id.fourthPickDateButton);
        fifthPickDateButton = (Button) view.findViewById(R.id.fifthPickDateButton);
        firstPickDateButton.setOnClickListener(this);
        secondPickDateButton.setOnClickListener(this);
        thirdPickDateButton.setOnClickListener(this);
        fourthPickDateButton.setOnClickListener(this);
        fifthPickDateButton.setOnClickListener(this);


        return new AlertDialog.Builder(getActivity())
                .setTitle("Set Time For Notification")
                .setView(view)
                .setPositiveButton(android.R.string.ok, null).create();
    }

    @Override
    public void onClick(View view) {
        DateTimePickerFragment fragment;
        int id = view.getId();

        switch (id) {
            case R.id.firstPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), 0);
                fragment.setTargetFragment(NotificationDialogFragment.this, FIRST_REQUEST_CODE);
                fragment.show(getFragmentManager(), "DATE_PICKER_FRAGMENT");
                break;
            case R.id.secondPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), 1);
                fragment.setTargetFragment(NotificationDialogFragment.this, SECOND_REQUEST_CODE);
                fragment.show(getFragmentManager(), "DATE_PICKER_FRAGMENT");
                break;
            case R.id.thirdPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), 2);
                fragment.setTargetFragment(NotificationDialogFragment.this, THIRD_REQUEST_CODE);
                fragment.show(getFragmentManager(), "DATE_PICKER_FRAGMENT");
                break;
            case R.id.fourthPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), 3);
                fragment.setTargetFragment(NotificationDialogFragment.this, FOURTH_REQUEST_CODE);
                fragment.show(getFragmentManager(), "DATE_PICKER_FRAGMENT");
                break;
            case R.id.fifthPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), 4);
                fragment.setTargetFragment(NotificationDialogFragment.this, FIFTH_REQUEST_CODE);
                fragment.show(getFragmentManager(), "DATE_PICKER_FRAGMENT");
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FIRST_REQUEST_CODE:
                    firstNotificationTextView.setText(event.getNotificationDateTime(0));
                    break;

                case SECOND_REQUEST_CODE:
                    secondNotificationTextView.setText(event.getNotificationDateTime(1));
                    break;

                case THIRD_REQUEST_CODE:
                    thirdNotificationTextView.setText(event.getNotificationDateTime(2));
                    break;

                case FOURTH_REQUEST_CODE:
                    fourthNotificationTextView.setText(event.getNotificationDateTime(3));
                    break;

                case FIFTH_REQUEST_CODE:
                    fifthNotificationTextView.setText(event.getNotificationDateTime(4));
                    break;

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("tag", "Noti-onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("tag", "Noti-onResume");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("tag", "Noti-onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("tag", "Noti-onDestroy");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("tag", "Noti-onDestroy");
    }
}
