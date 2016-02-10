package com.sophomoreventure.collegeconnect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sophomoreventure.collegeconnect.Activities.DrawerBaseActivity;
import com.sophomoreventure.collegeconnect.Activities.SlideShowActivity;
import com.sophomoreventure.collegeconnect.ModelClass.EventDatabase;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.adapters.ColorSpinnerAdapter;
import com.sophomoreventure.collegeconnect.extras.API;
import com.sophomoreventure.collegeconnect.extras.CloudinaryConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 24/12/2015.
 */
public class CreateEventActivity extends DrawerBaseActivity implements View.OnClickListener, DataListener {
    public static final int RESULT_LOAD_IMAGE = 0;
    public static final int PICKER_BUTTON_ID = R.id.pickerButton;
    public static final int CREATE_EVENT_BUTTON_ID = R.id.createEventButton;
    Cloudinary cloudinary;
    String imageName, imageUrl;
    ImageView eventImageView;
    Button imagePickerButton;
    Spinner themePicker;
    TextView nameEditText;
    EditText titleEditText;
    EditText descriptionEditText;
    EditText venueEditText;
    EditText clubNameTextView;
    TextView eventStartDateAndTimeTextView, eventEndDateAndTimeTextView, lastRegTextView;
    Button startDatePickButton, endDatePickButton, lastRegDateTimePickButton;

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
    boolean isImageChosen = false, isThemeSelected = false;
    long eventStartTime, eventEndTime, eventLastRegTime;
    Dialog spotsDialog;
    ViewGroup rootView;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private AlertDialog dialog;
    private String base64ImageString, picturePath, color;

    private String clubServerID = null;
    private EventDatabase database;
    private String colorCode;
    private Map imageResult;
    private DrawerLayout mDrawerLayout;

    public static boolean areAllFalse(boolean[] array) {
        for (boolean b : array) if (b) return false;
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_create_event);
        rootView = (ViewGroup) findViewById(R.id.createEventScrollView);
        event = new Event();
        eventHub = EventHub.getEventHub(this);
        eventId = event.getEventId().toString();
        eventHub.addEventToEventList(event);
        cloudinary = new Cloudinary(CloudinaryConfig.getConfig());
        String[] clubNames = getResources().getStringArray(R.array.club_list);
        clubServerID = getIntent().getStringExtra("clubId");
        database = new EventDatabase(this);
        if (clubServerID != null) {
            setEventDate(database.selectByEventId(clubServerID));
        }
        nameEditText = (TextView) findViewById(R.id.name);
        nameEditText.setVisibility(View.GONE);
        eventImageView = (ImageView) findViewById(R.id.eventImageView);
        eventImageView.setImageResource(R.drawable.placeholder);
        imagePickerButton = (Button) findViewById(R.id.pickerButton);


        titleEditText = (EditText) findViewById(R.id.titleEditText);
        titleEditText.clearFocus();
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        venueEditText = (EditText) findViewById(R.id.venueEditText);
        clubNameTextView = (EditText) findViewById(R.id.clubNameAutoCompleteTextView);


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
        dialog = new SpotsDialog(this, R.style.Create_Event_dialog);
        dialog.setCanceledOnTouchOutside(false);

        imagePickerButton.setOnClickListener(this);
        createEventButton.setOnClickListener(this);

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEditText.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        themePicker = (Spinner) findViewById(R.id.themeSpinner);
        String[] colorNames = {"Choose theme", "Blue", "Purple", "Blue Grey", "Teal"};
        final String[] colorList = {"#FFFFFF", "#2196f3", "#9c27b0", "#607d8b", "#009688"};

        themePicker.setAdapter(new ColorSpinnerAdapter(this, R.id.colorName, colorNames));
        themePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                color = parent.getItemAtPosition(position).toString();

                if (color.equalsIgnoreCase("Blue")) {
                    colorCode = "blue";
                    eventImageView.setImageResource(R.drawable.blue_gradient);
                    nameEditText.setVisibility(View.VISIBLE);
                    isThemeSelected = true;
                    isImageChosen = false;

                } else if ((color.equalsIgnoreCase("Purple"))) {
                    colorCode = "purple";
                    eventImageView.setImageResource(R.drawable.purple_gradient);
                    nameEditText.setVisibility(View.VISIBLE);
                    isThemeSelected = true;
                    isImageChosen = false;

                } else if ((color.equalsIgnoreCase("Blue Grey"))) {
                    colorCode = "bluegrey";
                    eventImageView.setImageResource(R.drawable.blue_grey_gradient);
                    nameEditText.setVisibility(View.VISIBLE);
                    isThemeSelected = true;
                    isImageChosen = false;

                } else if ((color.equalsIgnoreCase("Teal"))) {
                    colorCode = "teal";
                    eventImageView.setImageResource(R.drawable.teal_gradient);
                    nameEditText.setVisibility(View.VISIBLE);
                    isThemeSelected = true;
                    isImageChosen = false;

                } else if ((color.equalsIgnoreCase("Choose theme"))) {
                    colorCode = "null";

                    if (isThemeSelected) {
                        isThemeSelected = false;
                        eventImageView.setImageResource(R.drawable.placeholder);
                        nameEditText.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
        spotsDialog = new SpotsDialog(this, R.style.Create_Event_dialog);
        try {

            if (!getClubIdList().isEmpty()) {
                Event event = database.selectByEventId(getClubIdList().get(0).toString());
                clubNameTextView.setText(event.getEventClub());
                if(event != null){
                    event = null;
                }
            } else {
                clubNameTextView.setText("Individual");
            }

        } catch (JSONException e) {
            Log.i("tag", "" + e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_CREATE_EVENT;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
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
            isImageChosen = true;
            isThemeSelected = false;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
//            Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
            cursor.close();
            eventImageView.setImageURI(selectedImage);
            nameEditText.setVisibility(View.GONE);
            themePicker.setSelection(0);


        }
    }


    private void launchActivityDelayed(final Class activity) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(CreateEventActivity.this, activity));
            }
        }, 260);
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


            case CREATE_EVENT_BUTTON_ID:
                Arrays.fill(missingFields, Boolean.FALSE);

                if (isValidEvent()) {

                    imageName = titleEditText.getText().toString().toLowerCase()
                            + "By" + clubNameTextView.getText().toString();
                    imageUrl = cloudinary.url().generate(imageName);
                    Log.i("tag", imageName + " and " + imageUrl);
                    Log.i("tag", "" + EventUtility.getUserEmailFromPref(this));
                    Log.i("tag", "" + EventUtility.getUserPasswordHashFromPref(this));
                    final EditText confPassEditText = new EditText(CreateEventActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);

                    confPassEditText.setLayoutParams(lp);
                    confPassEditText.setHint("Your password");
                    confPassEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    orgTwoPhoneEditText.clearFocus();
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("Confirm Password")
                            .setView(confPassEditText)
                            .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("tag", confPassEditText.getText().toString());
                                    if (EventUtility.getHashString(confPassEditText.getText().toString(), "SHA-1")
                                            .equalsIgnoreCase(EventUtility.getUserPasswordHashFromPref(CreateEventActivity.this))) {

                                        try {
                                            spotsDialog.show();
                                            if (isImageChosen) {
                                                new PhotoUploadTask().execute(picturePath, imageName);
                                            } else {
                                                RequestorPost.requestCreateEvent(requestQueue, API.EVENT_API,
                                                        EventUtility.getUserEmailFromPref(CreateEventActivity.this),
                                                        EventUtility.getUserPasswordHashFromPref(CreateEventActivity.this), createJson(), CreateEventActivity.this);
                                            }



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        dialog.dismiss();
                                    } else {
                                        Snackbar.make(rootView, "Password doesn't share same opinion", Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();


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
            missingFields[8] = false;
//            orgTwoEmailEditText.setError("Invalid Email id");
        }

        if (!orgOnePhoneEditText.getText().toString().isEmpty() && orgOnePhoneEditText.getText().toString().length() < 10) {
            missingFields[9] = true;
            orgOnePhoneEditText.setError("Phone number is not valid");
        }

        if (!orgTwoPhoneEditText.getText().toString().isEmpty() && orgTwoPhoneEditText.getText().toString().length() < 10) {
            missingFields[10] = true;
            orgTwoPhoneEditText.setError("Phone number is not valid");
        }

        if (!isImageChosen && !isThemeSelected) {
            missingFields[11] = true;
            Snackbar.make(findViewById(R.id.createEventScrollView), "Select Image or Theme", Snackbar.LENGTH_LONG)
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
        jsonObject.put("seats", 0);
        jsonObject.put("lastregtime", eventLastRegTime / 1000);

        if (isImageChosen) {
            jsonObject.put("image", imageUrl);
        } else if (isThemeSelected) {
            jsonObject.put("color", colorCode);
        }


        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("contactname", orgOneEditText.getText().toString());
        object.put("contactnumber", Long.parseLong(orgOnePhoneEditText.getText().toString()));
        array.put(0, object);
        object.put("contactname", orgTwoEditText.getText().toString());
        object.put("contactnumber", Long.parseLong(orgTwoPhoneEditText.getText().toString()));
        array.put(1, object);
        jsonObject.put("contacts", array);

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
        bm.compress(Bitmap.CompressFormat.PNG, 10, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, 0);
        Log.i("tag", encodedImage);
        return encodedImage;
    }

    @Override
    public void onDataLoaded(String apiUrl) {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        View view = LayoutInflater.from(this).inflate(R.layout.done_message, null, false);
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Success")
                .setView(view)
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
    public void setError(String apiUrl, String errorCode) {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        Snackbar.make(rootView, "Internet is not working", Snackbar.LENGTH_LONG)
                .show();
//        if (errorCode.equals("NOCON")) {
//            new android.support.v7.app.AlertDialog.Builder(this)
//                    .setMessage("It seems your internet is not working working")
//                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .show();
//        }
    }

    public String getJsonString(Context context) throws FileNotFoundException {
        FileInputStream fis = context.openFileInput("clubs.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");

            }
            br.close();
        } catch (IOException e) {
            Log.i("tag", "" + e);
        }
        return stringBuilder.toString();
    }

    public ArrayList<Integer> getClubIdList() throws JSONException, FileNotFoundException {
        JSONArray jsonArray = new JSONArray(getJsonString(this));
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getInt(i));
            Log.i("vikas", "" + jsonArray.getInt(i));
        }
        return list;
    }

    private class PhotoUploadTask extends AsyncTask<String, Void, Map> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected Map doInBackground(String... filePath) {
            Map result = null;
            try {
                result = cloudinary.uploader().upload(filePath[0], ObjectUtils.asMap("public_id", filePath[1]));

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("vikas", e + "");
            }

            return result;
        }

        @Override
        protected void onPostExecute(Map result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (result != null) {
                if (result.containsKey("url")) {
                    Log.i("vikas", result.get("url").toString());
                    try {
                        RequestorPost.requestCreateEvent(requestQueue, API.EVENT_API,
                                EventUtility.getUserEmailFromPref(CreateEventActivity.this),
                                EventUtility.getUserPasswordHashFromPref(CreateEventActivity.this), createJson(), CreateEventActivity.this);
                    } catch (JSONException e) {
                        Log.i("tag", "" + e);
                    }
                    Toast.makeText(CreateEventActivity.this, "Photo uploded", Toast.LENGTH_SHORT).show();
                }
            } else {
                spotsDialog.dismiss();
                Snackbar.make(rootView, "Internet is not working", Snackbar.LENGTH_LONG)
                        .show();
            }


            imageResult = result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }


}
