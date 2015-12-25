package com.sophomoreventure.collegeconnect;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Murali on 25/12/2015.
 */
public class OrganizerDialogFragment extends DialogFragment implements AlertDialog.OnClickListener {

    private static final String KEY_EVENT_ID = "event_id";
    AlertDialog alertDialog;
    EditText orgOneEditText;
    EditText orgOnePhoneEditText;
    EditText orgTwoEditText;
    EditText orgTwoPhoneEditText;

    UUID eventId;


    EventHub eventHub;
    Event event;

    static OrganizerDialogFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT_ID, id);
        OrganizerDialogFragment fragment = new OrganizerDialogFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        eventId = UUID.fromString(getArguments().getString(KEY_EVENT_ID));
        eventHub = EventHub.getEventHub(getActivity());
        event = eventHub.getEventForId(eventId);

        View view = View.inflate(getActivity(), R.layout.fragment_oraganizer_dialog, null);
        orgOneEditText = (EditText) view.findViewById(R.id.orgOneEditText);
        orgOnePhoneEditText = (EditText) view.findViewById(R.id.orgOnePhoneEditText);
        orgTwoEditText = (EditText) view.findViewById(R.id.orgTwoEditText);
        orgTwoPhoneEditText = (EditText) view.findViewById(R.id.orgTwoPhoneEditText);

        if (event.getEventOrganizerOne() != null || event.getEventOrganizerOnePhoneNo() != null || event.getEventOrganizerTwo() != null || event.getEventOrganizerTwoPhoneNo() != null) {
            orgOneEditText.setText(event.getEventOrganizerOne());
            orgOnePhoneEditText.setText(event.getEventOrganizerOnePhoneNo());

            orgTwoEditText.setText(event.getEventOrganizerTwo());
            orgTwoPhoneEditText.setText(event.getEventOrganizerTwoPhoneNo());
        }
        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Set Organizers details")
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
                event.setEventOrganizerOne(orgOneEditText.getText().toString());
                event.setEventOrganizerOnePhoneNo(orgOnePhoneEditText.getText().toString());
                event.setEventOrganizerTwo(orgTwoEditText.getText().toString());
                event.setEventOrganizerTwoPhoneNo(orgTwoPhoneEditText.getText().toString());
                alertDialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                alertDialog.dismiss();
                break;
        }

    }
}
