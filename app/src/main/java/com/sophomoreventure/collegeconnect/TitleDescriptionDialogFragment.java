package com.sophomoreventure.collegeconnect;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.UUID;

/**
 * Created by Murali on 25/12/2015.
 */
public class TitleDescriptionDialogFragment extends DialogFragment implements AlertDialog.OnClickListener, View.OnClickListener {
    private static final String KEY_EVENT_ID = "event_id";
    private static final int REQUEST_CODE = 0;
    AlertDialog alertDialog;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText venueEditText;
    AutoCompleteTextView clubNameTextView;
    TextView eventDateAndTimeTextView;
    Button datePickButton;

    UUID eventId;


    EventHub eventHub;
    Event event;
    String[] clubNames;

    static TitleDescriptionDialogFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_ID, id);
        TitleDescriptionDialogFragment fragment = new TitleDescriptionDialogFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        eventId = UUID.fromString(getArguments().getString(KEY_EVENT_ID));
        eventHub = EventHub.getEventHub(getActivity());
        event = eventHub.getEventForId(eventId);
        clubNames = getResources().getStringArray(R.array.club_list);
        View view = View.inflate(getActivity(), R.layout.fragment_title_description_dialog, null);

        titleEditText = (EditText) view.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
        venueEditText = (EditText) view.findViewById(R.id.venueEditText);
        clubNameTextView = (AutoCompleteTextView) view.findViewById(R.id.clubNameAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, clubNames);
        clubNameTextView.setAdapter(adapter);

        eventDateAndTimeTextView = (TextView) view.findViewById(R.id.eventDateAndTime);
        datePickButton = (Button) view.findViewById(R.id.eventPickDateButton);
        datePickButton.setOnClickListener(this);

        if (event.getEventTitle() != null || event.getEventDescription() != null || event.getEventVenue() != null || event.getEventClub() != null) {
            titleEditText.setText(event.getEventTitle());
            descriptionEditText.setText(event.getEventDescription());
        }
        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Set Title and Description")
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this)
                .create();


        eventHub = EventHub.getEventHub(getActivity());
        event = eventHub.getEventForId(eventId);
        return alertDialog;

    }


    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                event.setEventTitle(titleEditText.getText().toString());
                event.setEventDescription(descriptionEditText.getText().toString());
                event.setEventClub(clubNameTextView.getText().toString());
                event.setEventVenue(venueEditText.getText().toString());
                alertDialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                alertDialog.dismiss();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        DateTimePickerFragment fragment;
        switch (v.getId()) {
            case R.id.eventPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), -1);
                fragment.setTargetFragment(TitleDescriptionDialogFragment.this, REQUEST_CODE);
                fragment.show(getFragmentManager(), "DATE_PICKER_FRAGMENT");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    eventDateAndTimeTextView.setText(EventUtility.getFriendlyDayString(event.getEventTime()));
                    break;
            }
        }
    }
}
