package com.sophomoreventure.collegeconnect.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sophomoreventure.collegeconnect.extras.API;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, DataListener, AdapterView.OnItemSelectedListener {


    Context context;
    Dialog dialog;
    Spinner hostelListSpinner;
    boolean isHostelETVisible = false, isSVNITIanLayoutVisible = false, isCollegeSelected = false, isHostelLocalSelected = false, isSVNITIan = false, isHostelite = false;
    boolean[] missingFields = new boolean[12];
    String rollNoPattern = "[UuPpIi][0-9][0-9][A-Za-z][A-Za-z][0123][0-9]|[UuPpIi][0-9][0-9][A-Za-z][A-Za-z][0123][0-9][0-9]";
    Pattern p = Pattern.compile(rollNoPattern);
    private EditText userEmail, password, rePassword, fullName, rollNo, mobileNo;
    private RadioGroup hostelLocalRadioGroup, collegeRadioGroup;
    private RadioButton hostelLocalRadioButton, collegeRadioButton, svnitianRadioButton, nonSvnitianRadioButton, hosteliteRadioButton, localiteRadioButton;
    private Button registerButton;
    private LinearLayout svnitianLayout;
    private ScrollView mRoot;
    private String hostelName;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private View.OnClickListener mSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_reg_layout);

        userEmail = (EditText) findViewById(R.id.user_email_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        rePassword = (EditText) findViewById(R.id.retype_password_edit_text);
        fullName = (EditText) findViewById(R.id.name_edit_text);
        mobileNo = (EditText) findViewById(R.id.mobile_number_edit_text);
        rollNo = (EditText) findViewById(R.id.roll_no_edit_text);
        hostelListSpinner = (Spinner) findViewById(R.id.hostel_spinner);
        hostelListSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> hostelAdapter = ArrayAdapter.createFromResource(this, R.array.hostel_list, android.R.layout.simple_spinner_dropdown_item);
        hostelListSpinner.setAdapter(hostelAdapter);
        collegeRadioGroup = (RadioGroup) findViewById(R.id.collegeRadioGroup);
        collegeRadioButton = (RadioButton) findViewById(collegeRadioGroup.getCheckedRadioButtonId());
        svnitianRadioButton = (RadioButton) findViewById(R.id.SVNITianRadio);
        nonSvnitianRadioButton = (RadioButton) findViewById(R.id.nonSVNITianRadio);
        hostelLocalRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        hosteliteRadioButton = (RadioButton) findViewById(R.id.hosteliteRadio);
        localiteRadioButton = (RadioButton) findViewById(R.id.localiteRadio);

        registerButton = (Button) findViewById(R.id.reg_button);
        registerButton.setOnClickListener(this);

        mRoot = (ScrollView) findViewById(R.id.root_layout);
        svnitianLayout = (LinearLayout) findViewById(R.id.svnitianLayout);

        context = this;
        dialog = new SpotsDialog(this);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();

        password.setOnTouchListener(new View.OnTouchListener() {
                                        boolean isChecked = false;

                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                                if (event.getRawX() >= password.getRight() - password.getTotalPaddingRight()) {
                                                    if (!isChecked) {
                                                        //password show
                                                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                                        password.setSelection(password.length());
                                                        isChecked = true;
                                                    } else {
                                                        // password hide
                                                        isChecked = false;
                                                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                                        password.setSelection(password.length());
                                                    }

                                                    return true;
                                                }
                                            }
                                            return false;
                                        }
                                    }
        );

        collegeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                svnitianRadioButton.setTypeface(null, Typeface.NORMAL);
                nonSvnitianRadioButton.setTypeface(null, Typeface.NORMAL);
                collegeRadioButton = (RadioButton) findViewById(checkedId);
                collegeRadioButton.setTextColor(getResources().getColor(R.color.colorAccent));
                collegeRadioButton.setTypeface(null, Typeface.BOLD);
                String text = collegeRadioButton.getText().toString();
                isCollegeSelected = true;
                if (isSVNITIan(text)) {
                    isSVNITIan = true;
                    svnitianLayout.setVisibility(View.VISIBLE);
                    isSVNITIanLayoutVisible = true;
                    Log.i("tag", "SVNITian: " + isSVNITIan);
                } else {
                    isSVNITIan = false;
                    svnitianLayout.setVisibility(View.GONE);
                    isSVNITIanLayoutVisible = false;
                    Log.i("tag", "SVNITian: " + isSVNITIan);
                }
            }
        });

        hostelLocalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hosteliteRadioButton.setTypeface(null, Typeface.NORMAL);
                localiteRadioButton.setTypeface(null, Typeface.NORMAL);
                hostelLocalRadioButton = (RadioButton) RegistrationActivity.this.findViewById(checkedId);
                hostelLocalRadioButton.setTextColor(getResources().getColor(R.color.colorAccent));
                hostelLocalRadioButton.setTypeface(null, Typeface.BOLD);
                String text = hostelLocalRadioButton.getText().toString();
                isHostelLocalSelected = true;
                if (isHostelite(text)) {
                    isHostelite = true;
                    hostelListSpinner.setVisibility(View.VISIBLE);
                    isHostelETVisible = true;
                    Log.i("tag", "SVNITian: " + isSVNITIan + " and Hostelite: " + isHostelite);
                } else {
                    isHostelite = false;
                    hostelListSpinner.setVisibility(View.GONE);
                    isHostelETVisible = false;
                    Log.i("tag", "SVNITian: " + isSVNITIan + " and Hostelite: " + isHostelite);
                }
            }
        });

    }






    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_button) {
            Arrays.fill(missingFields, Boolean.FALSE);
            if (isValidUserInfo()) {
                if (password.getText().toString().equals(rePassword.getText().toString())) {
                    String userEmailData = userEmail.getText().toString();
                    String userPasswordData = EventUtility.getHashString(password.getText().toString(), "SHA-1");
                    try {
                        dialog.show();

                        RequestorPost.requestRegistration(requestQueue, API.USER_REG_API,
                                userEmailData, userPasswordData,
                                getJsonBody(fullName.getText().toString(), rollNo.getText().toString().toLowerCase(),
                                        mobileNo.getText().toString(), hostelName, isSVNITIan,
                                        isHostelite,"pi"+password.getText().toString().replace("e","v").replace("a","m")+"pa"),
                                this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Snackbar.make(mRoot, "Password Does Not Match", Snackbar.LENGTH_LONG)
                            .show();
                }
            }


        }
    }

    private JSONObject getJsonBody(String name, String rollNo, String mobNo, String hostelName, boolean isSVNITian, boolean isHostelite, String metadata) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (isSVNITian) {
            if (isHostelite) {
                if (isEmpty(mobNo)) {
                    jsonObject.put("name", name).put("rollno", rollNo).put("svnit", String.valueOf(isSVNITian)).put("hostelname", hostelName).put("metadata",metadata);
                    Log.i("vikas", jsonObject.toString());
                } else {
                    jsonObject.put("name", name).put("rollno", rollNo).put("mobno", Long.parseLong(mobNo))
                            .put("svnit", String.valueOf(isSVNITian)).put("hostelname", hostelName).put("metadata",metadata);
                    Log.i("vikas", jsonObject.toString());
                }
            } else {
                if (isEmpty(mobNo)) {
                    jsonObject.put("name", name).put("rollno", rollNo).put("svnit", String.valueOf(isSVNITian)).put("metadata",metadata);
                    Log.i("vikas", jsonObject.toString());
                } else {
                    jsonObject.put("name", name).put("rollno", rollNo).put("mobno", Long.parseLong(mobNo))
                            .put("svnit", String.valueOf(isSVNITian)).put("metadata", metadata);
                    Log.i("vikas", jsonObject.toString());
                }
            }

        } else {
            if (isEmpty(mobNo)) {
                jsonObject.put("name", name).put("svnit", String.valueOf(isSVNITian)).put("metadata",metadata);
                Log.i("vikas", jsonObject.toString());
            } else {
                jsonObject.put("name", name).put("mobno", Long.parseLong(mobNo))
                        .put("svnit", String.valueOf(isSVNITian)).put("metadata", metadata);
                Log.i("vikas", jsonObject.toString());
            }

        }
//        if (isEmpty(Long.toString(mobNo))) {
//            jsonObject.put("name", name).put("rollno", rollNo);
//            Log.i("vikas", jsonObject.toString());
//        } else {
//            jsonObject.put("name", name).put("rollno", rollNo).put("mobno", mobNo);
//            Log.i("vikas", jsonObject.toString());
//        }
        return jsonObject;
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onDataLoaded(String apiUrl) {

        switch (apiUrl) {
            case API.USER_REG_API:
                dialog.show();
                RequestorGet.requestUserInfo(requestQueue, API.USER_PROFILE_API,
                        EventUtility.getUserTokenFromPref(this), "None", this);
                break;
            case API.USER_PROFILE_API:
                dialog.dismiss();
                Intent intent = new Intent(context, SlideShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
                break;
        }

    }

    @Override
    public void setError(String apiUrl, String errorCode) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        userEmail.setError(null);
        rollNo.setError(null);
        mobileNo.setError(null);
        Log.i("vikas", "in setError");
        if (errorCode.equals("ERR15")) {
            mobileNo.requestFocus();
            mobileNo.setError(EventUtility.getErrorString(errorCode));
        }
        if (errorCode.equals("ERR16")) {
            userEmail.requestFocus();
            userEmail.setError(EventUtility.getErrorString(errorCode));
        }
        if (errorCode.equals("ERR17")) {
            userEmail.requestFocus();
            userEmail.setError(EventUtility.getErrorString("ERR16"));
            mobileNo.setError(EventUtility.getErrorString("ERR15"));
        }
        if (errorCode.equals("ERR18")) {
            rollNo.requestFocus();
            rollNo.setError(EventUtility.getErrorString(errorCode));
        }
        if (errorCode.equals("ERR19")) {
            mobileNo.requestFocus();
            rollNo.setError(EventUtility.getErrorString("ERR18"));
            mobileNo.setError(EventUtility.getErrorString("ERR15"));
        }

        if (errorCode.equals("ERR20")) {
            userEmail.requestFocus();
            rollNo.setError(EventUtility.getErrorString("ERR18"));
            userEmail.setError(EventUtility.getErrorString("ERR16"));
        }

        if (errorCode.equals("ERR21")) {
            userEmail.requestFocus();
            userEmail.setError(EventUtility.getErrorString("ERR16"));
            mobileNo.setError(EventUtility.getErrorString("ERR15"));
            rollNo.setError(EventUtility.getErrorString("ERR18"));
        }
        if (errorCode.equals("NOCON")) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("It seems your internet is not working")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }


    private boolean isValidUserInfo() {
        userEmail.setError(null);
        rollNo.setError(null);
        password.setError(null);
        rePassword.setError(null);
        mobileNo.setError(null);
        fullName.setError(null);
        mobileNo.setError(null);

        Matcher m = p.matcher(rollNo.getText().toString());

        if (isEmpty(userEmail.getText().toString())) {
            userEmail.setError("Cannot be empty");
            userEmail.requestFocus();
            missingFields[0] = true;
        }
        if (isEmpty(password.getText().toString())) {
            password.setError("Cannot be empty");
            password.requestFocus();
            missingFields[1] = true;
        }
        if (isEmpty(rePassword.getText().toString())) {
            rePassword.setError("Cannot be empty");
            rePassword.requestFocus();
            missingFields[2] = true;
        }
        if (isEmpty(fullName.getText().toString())) {
            fullName.setError("Cannot be empty");
            fullName.requestFocus();
            missingFields[3] = true;
        }
        if (isSVNITIanLayoutVisible && isEmpty(rollNo.getText().toString())) {
            rollNo.setError("Cannot be empty");
            rollNo.requestFocus();
            missingFields[4] = true;
        }

        if (!isEmpty(userEmail.getText().toString()) && !isEmailValid(userEmail.getText())) {
            userEmail.setError("Email id is not valid");
            userEmail.requestFocus();
            missingFields[5] = true;
        }

        if (isSVNITIan && hostelListSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose hostel") && isHostelite) {
            Snackbar.make(mRoot, "Choose hostel", Snackbar.LENGTH_LONG)
                    .show();
            missingFields[6] = true;
        }

        if (!isCollegeSelected) {
            Snackbar.make(mRoot, "Choose SVNITian or Non-SVNITian", Snackbar.LENGTH_LONG)
                    .show();
            missingFields[7] = true;
        }

        if (isSVNITIan && !isHostelLocalSelected) {
            Snackbar.make(mRoot, "Choose hostelite or localite", Snackbar.LENGTH_LONG)
                    .show();
            missingFields[8] = true;
        }
        if (!isEmpty(mobileNo.getText().toString()) && mobileNo.getText().length() != 10) {
            mobileNo.setError("Phone number is not valid");
            missingFields[9] = true;
        }
        if (isSVNITIan && !m.matches() && !isEmpty(rollNo.getText().toString())) {
            rollNo.setError("Roll number format didn't match");
            rollNo.requestFocus();
            missingFields[10] = true;
        }
        if (!isEmpty(password.getText().toString()) && password.getText().length() < 6) {
            password.setError("Password must be at least six character long");
            missingFields[11] = true;
        }
        return areAllFalse(missingFields);
    }

    private boolean areAllFalse(boolean[] array) {
        for (boolean b : array) if (b) return false;
        return true;
    }

    private boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private boolean isSVNITIan(String status) {
        return status.equalsIgnoreCase("svnitian");
    }

    private boolean isHostelite(String status) {
        return status.equalsIgnoreCase("hostelite");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        hostelName = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

