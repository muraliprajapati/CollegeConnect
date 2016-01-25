package com.sophomoreventure.collegeconnect;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 24/12/2015.
 */
public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_LOAD_IMAGE = 0;
    public static final int PICKER_BUTTON_ID = R.id.pickerButton;
    public static final int NOTIFICATION_BUTTON_ID = R.id.notificationButton;
    public static final int DESCRIPTION_BUTTON_ID = R.id.descriptionButton;
    public static final int ORGANIZER_BUTTON_ID = R.id.organizerButton;
    public static final int CREATE_EVENT_BUTTON_ID = R.id.createEventButton;

    private static final int EVENT_NAME = 0;
    private static final int EVENT_CLUB = 1;
    private static final int EVENT_DESC = 2;
    private static final int EVENT_TIME = 3;
    private static final int EVENT_VENUE = 4;
    private static final int EVENT_ORG_ONE = 5;
    private static final int EVENT_ORG_ONE_PN = 6;
    private static final int EVENT_ORG_TWO = 7;
    private static final int EVENT_ORG_TWO_PN = 8;


    ImageView imageView;
    Button pickerButton;
    Button notificationButton;
    Button descriptionButton;
    Button organizerButton;
    Button createEventButton;

    FragmentManager manager;

    EventHub eventHub;
    Event event;
    String eventId;
    boolean validEvent = false;
    boolean[] missingFields = new boolean[9];
    StringBuilder toastString = new StringBuilder("Missing Fields:").append("\n");
    private AlertDialog dialog;

    public static boolean areAllFalse(boolean[] array) {
        for (boolean b : array) if (b) return false;
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        event = new Event();
        eventHub = EventHub.getEventHub(this);
        eventId = event.getEventId().toString();
        eventHub.addEventToEventList(event);

        Arrays.fill(missingFields, Boolean.FALSE);
        manager = getFragmentManager();
        imageView = (ImageView) findViewById(R.id.imageView);
        pickerButton = (Button) findViewById(R.id.pickerButton);
        notificationButton = (Button) findViewById(R.id.notificationButton);
        descriptionButton = (Button) findViewById(R.id.descriptionButton);
        organizerButton = (Button) findViewById(R.id.organizerButton);
        createEventButton = (Button) findViewById(R.id.createEventButton);
        dialog = new SpotsDialog(this);
        dialog.setMessage("Posting event");
        dialog.setCanceledOnTouchOutside(false);
        pickerButton.setOnClickListener(this);
        notificationButton.setOnClickListener(this);
        descriptionButton.setOnClickListener(this);
        organizerButton.setOnClickListener(this);
        createEventButton.setOnClickListener(this);

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
            case CREATE_EVENT_BUTTON_ID:
                Arrays.fill(missingFields, Boolean.FALSE);
                validateEvent();
                validEvent = areAllFalse(missingFields);
                if (validEvent) {
                    Log.i("tag", "event valid");
                    showArray(missingFields);
                    EventRegTask task = new EventRegTask();
                    task.execute();
                    dialog.show();
//                    Toast.makeText(CreateEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("tag", "event invalid");
                    showArray(missingFields);
                    showErrorToast();
                }

                break;
        }
    }

    private void validateEvent() {

        if (event.getEventTitle().isEmpty() || event.getEventTitle() == null) {
            missingFields[EVENT_NAME] = true;
            toastString.append("Event Name").append("\n");
        }
        if (event.getClubOfEvent().isEmpty() || event.getClubOfEvent() == null) {
            missingFields[EVENT_CLUB] = true;
            toastString.append("Event club").append("\n");
        }
        if (event.getEventDescription().isEmpty() || event.getEventDescription() == null) {
            missingFields[EVENT_DESC] = true;
            toastString.append("Event description").append("\n");
        }
        if (Long.toString(event.getEventTime()).isEmpty() || event.getEventTime() == 0) {
            missingFields[EVENT_TIME] = true;
            toastString.append("Event time").append("\n");
        }
        if (event.getEventVenue().isEmpty() || event.getEventVenue() == null) {
            missingFields[EVENT_VENUE] = true;
            toastString.append("Event venue").append("\n");
        }
        if (event.getEventOrganizerOne().isEmpty() || event.getEventOrganizerOne() == null) {
            missingFields[EVENT_ORG_ONE] = true;
            toastString.append("Event organizer one").append("\n");
        }
        if (event.getEventOrganizerOnePhoneNo().isEmpty() || event.getEventOrganizerOnePhoneNo() == null) {
            missingFields[EVENT_ORG_ONE_PN] = true;
            toastString.append("Event organizer one phone no.").append("\n");
        }
        if (event.getEventOrganizerTwo().isEmpty() || event.getEventOrganizerTwo() == null) {
            missingFields[EVENT_ORG_TWO] = true;
            toastString.append("Event organizer two").append("\n");
        }
        if (event.getEventOrganizerTwoPhoneNo().isEmpty() || event.getEventOrganizerTwoPhoneNo() == null) {
            missingFields[EVENT_ORG_TWO_PN] = true;
            toastString.append("Event organizer two phone no.").append("\n");
        }


    }

    private void showErrorToast() {
        Toast.makeText(this, toastString.toString(), Toast.LENGTH_LONG).show();
        toastString.delete(0, toastString.length());
        toastString.append("Missing Fields:").append("\n");
    }

    private void showArray(boolean[] array) {
        for (boolean anArray : array) {
            Log.i("tag", "" + anArray);
        }
    }

    private class EventRegTask extends AsyncTask<Void, Void, Void> {
        JSONObject jsonObject = new JSONObject();

        @Override
        protected Void doInBackground(Void... voids) {

            String urlString = API.EVENT_API;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);


                String userCredentials = "123:123";
                final String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setUseCaches(false);
                byte[] outputBytes = createJson().toString().getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);
                connection.connect();

                int responseCode = connection.getResponseCode();
                //String responseMsg = connection.getResponseMessage();
                Log.i("tag", "RESPONSE CODE: " + responseCode);

                if (responseCode == 200) {
                    InputStream inputStr = connection.getInputStream();
                    String encoding = connection.getContentEncoding() == null ? "UTF-8"
                            : connection.getContentEncoding();
                    String jsonResponse = IOUtils.toString(inputStr, encoding);
                    Log.i("tag", jsonResponse);


                }

            } catch (Exception e) {
                Log.d("tag", e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Toast.makeText(CreateEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
            cancel(true);

        }

        JSONObject createJson() throws JSONException {
            jsonObject.put("name", event.getEventTitle());
            jsonObject.put("about", event.getEventDescription());
            jsonObject.put("sdt", event.getEventTime() / 1000);
            jsonObject.put("edt", event.getEventTime() / 1000);
            jsonObject.put("venue", event.getEventVenue());
            jsonObject.put("seats", 50);
            jsonObject.put("lastregtime", event.getEventTime() / 1000);
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("contactname", event.getEventOrganizerOne());
            object.put("contactnumber", Integer.parseInt(event.getEventOrganizerOnePhoneNo()));
            array.put(0, object);
            object.put("contactname", event.getEventOrganizerTwo());
            object.put("contactnumber", Integer.parseInt(event.getEventOrganizerTwoPhoneNo()));
            array.put(1, object);
            jsonObject.put("contacts", array);
            Log.i("tag", jsonObject.toString());
            return jsonObject;
        }

    }

}
