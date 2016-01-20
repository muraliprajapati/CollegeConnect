package com.sophomoreventure.collegeconnect.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 30/12/2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailEditText;
    EditText passEditText;
    EditText guestNameEditText;
    EditText guestCollegeEditText;
    Button loginButton;
    Button regButton;
    Button guestLoginButton;
    Button guestFinalLoginButton;
    LinearLayout guestCradLayout;
    boolean isLayoutVisibile = false;
    LoginTask task = new LoginTask();
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String userName = null;
    private String userPassword = null;
    private Context context;
    private TextView circular;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        guestCradLayout = (LinearLayout) findViewById(R.id.guestCradLayout);
        emailEditText = (EditText) findViewById(R.id.input_email);
        passEditText = (EditText) findViewById(R.id.input_password);
        guestNameEditText = (EditText) findViewById(R.id.guest_name_edit_text);
        guestCollegeEditText = (EditText) findViewById(R.id.guest_college_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);
        regButton = (Button) findViewById(R.id.register_button);
        guestLoginButton = (Button) findViewById(R.id.guest_login_button);
        guestFinalLoginButton = (Button) findViewById(R.id.guest_final_login_button);
        dialog = new SpotsDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        context = this;


        regButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        guestLoginButton.setOnClickListener(this);
        guestFinalLoginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String userName = emailEditText.getText().toString();
        String password = passEditText.getText().toString();
        String guestName = guestNameEditText.getText().toString();
        String guestCollege = guestCollegeEditText.getText().toString();
        int id = v.getId();
        switch (id) {
            case R.id.login_button:
                if (isEmptyUserName(userName) || isEmptyPassword(password)) {
                    emailEditText.setError("Username cannot be empty");
                    emailEditText.requestFocus();
                    passEditText.setError("Password cannot be empty");

                } else if (isEmptyUserName(userName)) {
                    emailEditText.setError("Username cannot be empty");
                    emailEditText.requestFocus();
                } else if (isEmptyPassword(password)) {
                    passEditText.setError("Password cannot be empty");
                    passEditText.requestFocus();
                } else {


                    task.execute(userName, password);
                    dialog.show();
                }

                break;
            case R.id.register_button:
                startActivity(new Intent(v.getContext(), RegistrationActivity.class));
                break;

            case R.id.guest_login_button:
                if (isLayoutVisibile) {
                    guestCradLayout.setVisibility(View.GONE);
                    isLayoutVisibile = false;
                } else {
                    guestCradLayout.setVisibility(View.VISIBLE);
                    isLayoutVisibile = true;
                }

                break;
            case R.id.guest_final_login_button:
                if (isEmptyGuestName(guestName) || isEmptyGuestCollege(guestCollege)) {
                    guestNameEditText.setError("Name cannot be empty");
                    guestNameEditText.requestFocus();
                    guestCollegeEditText.setError("College cannot be empty");

                } else if (isEmptyGuestName(guestName)) {
                    guestNameEditText.setError("Name cannot be empty");
                    guestNameEditText.requestFocus();
                } else if (isEmptyGuestCollege(guestCollege)) {
                    guestCollegeEditText.setError("College cannot be empty");
                    guestCollegeEditText.requestFocus();
                } else {
//                    Toast.makeText(LoginActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                    dialog.show();
                }

                break;
        }
    }

    private boolean isEmptyUserName(String userName) {

        return TextUtils.isEmpty(userName);
    }

    private boolean isEmptyPassword(String userPassword) {

        return TextUtils.isEmpty(userPassword);
    }

    private boolean isEmptyGuestName(String userName) {

        return TextUtils.isEmpty(userName);
    }

    private boolean isEmptyGuestCollege(String collegeName) {

        return TextUtils.isEmpty(collegeName);
    }

    private void parseAndSaveToPref(String userName, String userPassword, String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String userToken = jsonObject.getString("token");
        SharedPreferences prefs = context.getSharedPreferences(Constants.SharedPrefConstants.USER_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.SharedPrefConstants.USER_SHARED_PREF_LOGGED_IN_KEY, true);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_NAME_KEY, userName);
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_PASSWORD_KEY, EventUtility.getHashString(userPassword, "SHA-1"));
        editor.putString(Constants.SharedPrefConstants.USER_SHARED_PREF_USER_TOKEN_KEY, userToken);
        editor.apply();
        Log.i("tag", userToken);
        Log.i("tag", EventUtility.getHashString(userPassword, "SHA-1"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        task.cancel(true);
        finish();
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            //RequestorPost.requestStringData(requestQueue, API.USER_REG_API, userName, userPassword);

            String urlString = API.USER_REG_API;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                Log.i("tag", params[0] + ":" + params[1]);

                String userCredentials = params[0] + ":" + params[1];
                final String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.connect();

                int responseCode = connection.getResponseCode();
                //String responseMsg = connection.getResponseMessage();
                Log.i("tag", "Authentication: " + basicAuth);
                Log.i("tag", "RESPONSE CODE: " + responseCode);

                if (responseCode == 200) {
                    InputStream inputStr = connection.getInputStream();
                    String encoding = connection.getContentEncoding() == null ? "UTF-8"
                            : connection.getContentEncoding();
                    String jsonResponse = IOUtils.toString(inputStr, encoding);

                    parseAndSaveToPref(params[0], params[1], jsonResponse);
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
            if (dialog.isShowing()) {
                dialog.dismiss();
                cancel(true);
                Intent intent = new Intent(LoginActivity.this, SlideShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }
    }
}
