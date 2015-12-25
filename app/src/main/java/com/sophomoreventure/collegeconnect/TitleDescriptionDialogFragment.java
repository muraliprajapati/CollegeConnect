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
public class TitleDescriptionDialogFragment extends DialogFragment implements AlertDialog.OnClickListener {
    private static final String KEY_EVENT_ID = "event_id";
    AlertDialog alertDialog;
    EditText titleEditText;
    EditText descriptionEditText;

    UUID eventId;


    EventHub eventHub;
    Event event;

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

        View view = View.inflate(getActivity(), R.layout.fragment_title_description_dialog, null);
        titleEditText = (EditText) view.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);

        if (event.getEventTitle() != null || event.getEventDescription() != null) {
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
                alertDialog.dismiss();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                alertDialog.dismiss();
                break;
        }

    }
}
