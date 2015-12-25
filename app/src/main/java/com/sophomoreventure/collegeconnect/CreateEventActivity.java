package com.sophomoreventure.collegeconnect;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Murali on 24/12/2015.
 */
public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_LOAD_IMAGE = 0;
    public static final int PICKER_BUTTON_ID = R.id.pickerButton;
    public static final int NOTIFICATION_BUTTON_ID = R.id.notificationButton;
    public static final int DESCRIPTION_BUTTON_ID = R.id.descriptionButton;
    public static final int ORGANIZER_BUTTON_ID = R.id.organizerButton;

    ImageView imageView;
    Button pickerButton;
    Button notificationButton;
    Button descriptionButton;
    Button organizerButton;

    FragmentManager manager;

    EventHub eventHub;
    Event event = new Event();
    String eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventHub = EventHub.getEventHub(this);
        eventId = event.getEventId().toString();
        eventHub.addEventToEventList(event);

        manager = getFragmentManager();
        imageView = (ImageView) findViewById(R.id.imageView);
        pickerButton = (Button) findViewById(R.id.pickerButton);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        descriptionButton = (Button) findViewById(R.id.descriptionButton);
        organizerButton = (Button) findViewById(R.id.organizerButton);

        pickerButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        descriptionButton.setOnClickListener(this);
        organizerButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageURI(selectedImage);

        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case PICKER_BUTTON_ID:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;

            case NOTIFICATION_BUTTON_ID:
                NotificationDialogFragment notificationDialogFragment = NotificationDialogFragment.newInstance(eventId);
                notificationDialogFragment.show(manager, "NOTIFICATION_DIALOG_FRAGMENT");
                break;

            case DESCRIPTION_BUTTON_ID:
                TitleDescriptionDialogFragment titleDescriptionDialogFragment = TitleDescriptionDialogFragment.newInstance(eventId);
                titleDescriptionDialogFragment.show(manager, "TITLE_DESCRIPTION_FRAGMENT");
                break;

            case ORGANIZER_BUTTON_ID:
                OrganizerDialogFragment organizerDialogFragment = OrganizerDialogFragment.newInstance(eventId);
                organizerDialogFragment.show(manager, "ORGANIZER_FRAGMENT");
                break;
        }
    }
}
