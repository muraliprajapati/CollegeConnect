package com.sophomoreventure.collegeconnect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Murali on 19/01/2016.
 */
public class SvnitRegActivity extends AppCompatActivity implements View.OnClickListener, DataListener {
    String userName, password;
    EditText fullName, rollNo, mobileNo, hostelName, email;
    CheckBox hosteliteCheckBox, localiteCheckBox;
    Button registerButton;
    boolean[] missingFields = new boolean[3];
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_svnit_reg_layout);
        userName = getIntent().getStringExtra("user_name");
        password = getIntent().getStringExtra("password");
        fullName = (EditText) findViewById(R.id.name_edit_text);
        rollNo = (EditText) findViewById(R.id.roll_no_edit_text);
        mobileNo = (EditText) findViewById(R.id.mobile_number_edit_text);
        email = (EditText) findViewById(R.id.email_edit_text);
        hosteliteCheckBox = (CheckBox) findViewById(R.id.hostelite_check_box);
        localiteCheckBox = (CheckBox) findViewById(R.id.localite_check_box);
        hostelName = (EditText) findViewById(R.id.hostel_name_edit_text);
        registerButton = (Button) findViewById(R.id.reg_button);
        registerButton.setOnClickListener(this);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_button) {
            Arrays.fill(missingFields, Boolean.FALSE);

            if (isValidUserInfo()) {
                if (isEmailValid(email.getText())) {
                    try {
                        RequestorPost.requestRegistration(requestQueue, API.USER_REG_API, userName, password, getJsonBody(fullName.getText().toString(),
                                email.getText().toString(), rollNo.getText().toString(), Integer.parseInt(mobileNo.getText().toString())), true, this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    email.setError("Email is not valid");
                }

            }
        }
    }


    private boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidUserInfo() {
        if (isEmpty(fullName.getText().toString())) {
            fullName.setError("Cannot be empty");
            missingFields[0] = true;
        }
        if (isEmpty(rollNo.getText().toString())) {
            rollNo.setError("Cannot be empty");
            missingFields[1] = true;
        }
        if (isEmpty(email.getText().toString())) {
            email.setError("Cannot be empty");
            missingFields[2] = true;
        }

        return areAllFalse(missingFields);
    }

    private boolean areAllFalse(boolean[] array) {
        for (boolean b : array) if (b) return false;
        return true;
    }

    private JSONObject getJsonBody(String name, String email, String rollNo, int mobNo) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (isEmpty(Integer.toString(mobNo))) {
            jsonObject.put("name", name).put("rollno", rollNo).put("email", email);
            Log.i("vikas", jsonObject.toString());
        } else {
            jsonObject.put("name", name).put("rollno", rollNo).put("email", email).put("mobno", mobNo);
            Log.i("vikas", jsonObject.toString());
        }
        return jsonObject;
    }

    @Override
    public void onDataLoaded(boolean response) {

    }

    @Override
    public void setError(String errorCode) {
        email.setError(null);
        rollNo.setError(null);
        mobileNo.setError(null);
        Log.i("vikas", "in setError");
        if (errorCode.equals("ERR15")) {
            mobileNo.setError(EventUtility.getErrorString(errorCode));
        }
        if (errorCode.equals("ERR16")) {
            email.setError(EventUtility.getErrorString(errorCode));
        }
        if (errorCode.equals("ERR17")) {
            email.setError(EventUtility.getErrorString("ERR16"));
            mobileNo.setError(EventUtility.getErrorString("ERR15"));
        }
        if (errorCode.equals("ERR18")) {
            rollNo.setError(EventUtility.getErrorString(errorCode));
        }
        if (errorCode.equals("ERR19")) {
            rollNo.setError(EventUtility.getErrorString("ERR18"));
            mobileNo.setError(EventUtility.getErrorString("ERR15"));
        }

        if (errorCode.equals("ERR20")) {
            rollNo.setError(EventUtility.getErrorString("ERR18"));
            email.setError(EventUtility.getErrorString("ERR16"));
        }

        if (errorCode.equals("ERR21")) {
            email.setError(EventUtility.getErrorString("ERR16"));
            mobileNo.setError(EventUtility.getErrorString("ERR15"));
            rollNo.setError(EventUtility.getErrorString("ERR18"));
        }

    }
}
