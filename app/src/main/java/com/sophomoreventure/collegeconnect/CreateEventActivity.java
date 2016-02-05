package com.sophomoreventure.collegeconnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 24/12/2015.
 */
public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, DataListener {
    public static final int RESULT_LOAD_IMAGE = 0;
    public static final int PICKER_BUTTON_ID = R.id.pickerButton;
    public static final int CREATE_EVENT_BUTTON_ID = R.id.createEventButton;


    ImageView eventImageView;
    Button imagePickerButton;
    Spinner themePicker;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText venueEditText;
    AutoCompleteTextView clubNameTextView;
    TextView eventStartDateAndTimeTextView, eventEndDateAndTimeTextView, lastRegTextView;
    Button startDatePickButton, endDatePickButton, lastRegDateTimePickButton, clearImageButton;
    EditText orgOneEditText;
    EditText orgOneEmailEditText;
    EditText orgOnePhoneEditText;
    EditText orgTwoEditText;
    EditText orgTwoEmailEditText;
    EditText orgTwoPhoneEditText;
    Button createEventButton;
    EventHub eventHub;
    Event event;
    String eventId;
    boolean[] missingFields = new boolean[12];
    boolean isImageChoosen = false, isThemeSelected = false;
    long eventStartTime, eventEndTime, eventLastRegTime;
    Dialog spotsDialog;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private AlertDialog dialog;
    private String base64ImageString, picturePath, color;

    private String clubServerID = null;
    private EventDatabase database;

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

        String[] clubNames = getResources().getStringArray(R.array.club_list);
        clubServerID = getIntent().getStringExtra("clubId");
        database = new EventDatabase(this);
        if(clubServerID != null){
            setEventDate(database.selectByEventId(clubServerID));
        }
        eventImageView = (ImageView) findViewById(R.id.eventImageView);
        imagePickerButton = (Button) findViewById(R.id.pickerButton);
        clearImageButton = (Button) findViewById(R.id.clearImageButton);
        clearImageButton.setVisibility(View.GONE);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        venueEditText = (EditText) findViewById(R.id.venueEditText);
        clubNameTextView = (AutoCompleteTextView) findViewById(R.id.clubNameAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clubNames);
        clubNameTextView.setAdapter(adapter);

        eventStartDateAndTimeTextView = (TextView) findViewById(R.id.eventDateAndTime);
        startDatePickButton = (Button) findViewById(R.id.eventPickDateButton);
        startDatePickButton.setOnClickListener(this);

        eventEndDateAndTimeTextView = (TextView) findViewById(R.id.eventEndDateAndTime);
        endDatePickButton = (Button) findViewById(R.id.eventPickEndDateButton);
        endDatePickButton.setOnClickListener(this);

        orgOneEditText = (EditText) findViewById(R.id.orgOneEditText);
        orgOneEmailEditText = (EditText) findViewById(R.id.orgOneEmailEditText);
        orgOnePhoneEditText = (EditText) findViewById(R.id.orgOnePhoneEditText);
        orgTwoEditText = (EditText) findViewById(R.id.orgTwoEditText);
        orgTwoEmailEditText = (EditText) findViewById(R.id.orgTwoEmailEditText);
        orgTwoPhoneEditText = (EditText) findViewById(R.id.orgTwoPhoneEditText);

        lastRegTextView = (TextView) findViewById(R.id.lastRegTextView);
        lastRegDateTimePickButton = (Button) findViewById(R.id.eventLastRegDateButton);
        lastRegDateTimePickButton.setOnClickListener(this);


        createEventButton = (Button) findViewById(R.id.createEventButton);
        dialog = new SpotsDialog(this);
        dialog.setMessage("Posting event");
        dialog.setCanceledOnTouchOutside(false);

        imagePickerButton.setOnClickListener(this);
        clearImageButton.setOnClickListener(this);
        createEventButton.setOnClickListener(this);

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        themePicker = (Spinner) findViewById(R.id.themeSpinner);
        String[] colorNames = {"Choose theme", "Red", "Blue", "Green", "Orange"};

        themePicker.setAdapter(new ColorSpinnerAdapter(this, R.id.colorName, colorNames));
        themePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                color = parent.getItemAtPosition(position).toString();

                GradientDrawable gd = (GradientDrawable) eventImageView.getBackground();
                gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//                gd.setOrientation(GradientDrawable.Orientation.TL_BR);
//
//                if (color.equalsIgnoreCase("red")) {
                isThemeSelected = true;
//                    int[] colors = {Color.parseColor("#f44336"), Color.parseColor("#e57373")};
//                    gd.setColors(colors);
//                } else if ((color.equalsIgnoreCase("blue"))) {
                isThemeSelected = true;
//                    int[] colors = {Color.parseColor("#3f51b5"), Color.parseColor("#7986cb")};
//                    gd.setColors(colors);
//                } else if ((color.equalsIgnoreCase("green"))) {
                isThemeSelected = true;
//                    int[] colors = {Color.parseColor("#4caf50"), Color.parseColor("#81c784")};
//                    gd.setColors(colors);
//                } else if ((color.equalsIgnoreCase("orange"))) {
                isThemeSelected = true;
//                    int[] colors = {Color.parseColor("#ff5722"), Color.parseColor("#ff8a65")};
//                    gd.setColors(colors);
//                }else if ((color.equalsIgnoreCase("Choose theme"))) {
                isThemeSelected = false;
//                    int[] colors = {Color.parseColor("#ff5722"), Color.parseColor("#ff8a65")};
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
        spotsDialog = new SpotsDialog(this, R.style.Login_dialog);
    }

    private void setEventDate(Event event) {
        titleEditText.setText(event.getEventTitle());
        descriptionEditText.setText(event.getEventDescription());
        venueEditText.setText(event.getEventVenue());
        clubNameTextView.setText(event.getEventClub());
        orgOneEditText.setText(event.getEventOrganizerOne());
        orgOneEmailEditText.setText(event.getOrganizerEmailOne());
        orgOnePhoneEditText.setText(event.getEventOrganizerOnePhoneNo());
        orgTwoEditText.setText(event.getEventOrganizerTwo());
        orgTwoEmailEditText.setText(event.getOrganizerEmailTwo());
        orgTwoPhoneEditText.setText(event.getEventOrganizerTwoPhoneNo());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            isImageChoosen = true;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
            cursor.close();
            eventImageView.setImageURI(selectedImage);
            clearImageButton.setVisibility(View.VISIBLE);
            themePicker.setClickable(false);
            themePicker.setEnabled(false);
            base64ImageString = convertImageToBase64(picturePath);

        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        DateTimePickerFragment fragment;

        switch (id) {
            case PICKER_BUTTON_ID:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;

            case R.id.clearImageButton:
                eventImageView.setImageResource(0);
                clearImageButton.setVisibility(View.GONE);
                themePicker.setClickable(true);
                themePicker.setEnabled(true);
                break;

            case CREATE_EVENT_BUTTON_ID:
                Arrays.fill(missingFields, Boolean.FALSE);


                if (isValidEvent()) {
                    try {
                        spotsDialog.show();

                        RequestorPost.requestCreateEvent(requestQueue, API.EVENT_API,
                                EventUtility.getUserEmailFromPref(this),
                                EventUtility.getUserPasswordHashFromPref(this), createJson(), this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    Toast.makeText(CreateEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("tag", "event invalid");
                    showArray(missingFields);
                }

                break;

            case R.id.eventPickDateButton:
                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), R.id.eventPickDateButton);
                fragment.show(getFragmentManager(), "START_DATE_PICKER_FRAGMENT");
                break;

            case R.id.eventPickEndDateButton:

                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), R.id.eventPickEndDateButton);
                fragment.show(getFragmentManager(), "END_DATE_PICKER_FRAGMENT");
                break;

            case R.id.eventLastRegDateButton:

                fragment = DateTimePickerFragment.newInstance(event.getEventId().toString(), R.id.eventLastRegDateButton);
                fragment.show(getFragmentManager(), "LAST_REG_DATE_PICKER_FRAGMENT");
                break;
        }
    }

    public void setDateTime(long dateTimeInMillis, int id) {
        switch (id) {
            case R.id.eventPickDateButton:
                eventStartTime = dateTimeInMillis;
                eventStartDateAndTimeTextView.setText(EventUtility.getFriendlyDayString(dateTimeInMillis));
                break;

            case R.id.eventPickEndDateButton:
                eventEndTime = dateTimeInMillis;
                eventEndDateAndTimeTextView.setText(EventUtility.getFriendlyDayString(dateTimeInMillis));
                break;

            case R.id.eventLastRegDateButton:
                eventLastRegTime = dateTimeInMillis;
                lastRegTextView.setText(EventUtility.getFriendlyDayString(dateTimeInMillis));
                break;
        }

    }

    private boolean isValidEvent() {
        clearErrors();

        if (titleEditText.getText().toString().isEmpty()) {
            missingFields[0] = true;
            titleEditText.setError("Cannot be empty");
        }

        if (descriptionEditText.getText().toString().isEmpty()) {
            missingFields[1] = true;
            descriptionEditText.setError("Cannot be empty");
        }
        if (eventStartDateAndTimeTextView.getText().toString().equalsIgnoreCase("Event Start Date and Time")) {
            missingFields[2] = true;
            Snackbar.make(findViewById(R.id.createEventScrollView), "Set Event Date and Time", Snackbar.LENGTH_LONG)
                    .show();
        }
        if (venueEditText.getText().toString().isEmpty()) {
            missingFields[3] = true;
            venueEditText.setError("Cannot be empty");
        }
        if (orgOneEditText.getText().toString().isEmpty()) {
            missingFields[4] = true;
            orgOneEditText.setError("Cannot be empty");
        }
        if (orgOnePhoneEditText.getText().toString().isEmpty()) {
            missingFields[5] = true;
            orgOnePhoneEditText.setError("Cannot be empty");
        }

        if (orgTwoEditText.getText().toString().isEmpty()) {
            missingFields[6] = true;
            orgTwoEditText.setError("Cannot be empty");
        }
        if (orgTwoPhoneEditText.getText().toString().isEmpty()) {
            missingFields[7] = true;
            orgTwoPhoneEditText.setError("Cannot be empty");
        }
        if (!orgTwoEmailEditText.getText().toString().isEmpty() && !isEmailValid(orgTwoEmailEditText.getText().toString())) {
            missingFields[8] = true;
            orgTwoEmailEditText.setError("Invalid Email id");
        }

        if (!orgOnePhoneEditText.getText().toString().isEmpty() && orgOnePhoneEditText.getText().toString().length() < 10) {
            missingFields[9] = true;
            orgOnePhoneEditText.setError("Phone number is not valid");
        }

        if (!orgTwoPhoneEditText.getText().toString().isEmpty() && orgTwoPhoneEditText.getText().toString().length() < 10) {
            missingFields[10] = true;
            orgTwoPhoneEditText.setError("Phone number is not valid");
        }

        if (!isImageChoosen && !isThemeSelected) {
            missingFields[11] = true;
            Snackbar.make(findViewById(R.id.createEventScrollView), "Select Poster or Theme", Snackbar.LENGTH_LONG)
                    .show();
        }

        return areAllFalse(missingFields);
    }

    private void showArray(boolean[] array) {
        for (boolean anArray : array) {
            Log.i("tag", "" + anArray);
        }
    }

    private JSONObject createJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", titleEditText.getText().toString());
        jsonObject.put("about", descriptionEditText.getText().toString());
        jsonObject.put("sdt", eventStartTime / 1000);
        jsonObject.put("edt", eventEndTime / 1000);
        jsonObject.put("venue", venueEditText.getText().toString());
        jsonObject.put("seats", 50);
        jsonObject.put("lastregtime", eventLastRegTime / 1000);


        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("contactname", orgOneEditText.getText().toString());
        object.put("contactnumber", Long.parseLong(orgOnePhoneEditText.getText().toString()));
        array.put(0, object);
        object.put("contactname", orgTwoEditText.getText().toString());
        object.put("contactnumber", Long.parseLong(orgTwoPhoneEditText.getText().toString()));
        array.put(1, object);
        jsonObject.put("contacts", array);
//        jsonObject.put("image", base64ImageString);
        Log.i("tag", jsonObject.toString());
        return jsonObject;
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void clearErrors() {
        titleEditText.setError(null);
        descriptionEditText.setError(null);
        venueEditText.setError(null);
        orgOneEditText.setError(null);
        orgOneEmailEditText.setError(null);
        orgOnePhoneEditText.setError(null);
        orgTwoEditText.setError(null);
        orgTwoEmailEditText.setError(null);
        orgTwoPhoneEditText.setError(null);
    }

    private String convertImageToBase64(String imageLocation) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(imageLocation.trim());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, 0);
        Log.i("tag", encodedImage);
        return encodedImage;
    }

    @Override
    public void onDataLoaded(boolean response) {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage("Your Event has been posted successfully")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateEventActivity.this, SlideShowActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        CreateEventActivity.this.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void setError(String errorCode) {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        if (errorCode.equals("NOCON")) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("It seems your internet is not working working")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
