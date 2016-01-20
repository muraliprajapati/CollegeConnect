package com.sophomoreventure.collegeconnect.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.NonSvnitRegActivity;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.SvnitRegActivity;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, DataListener {

    HttpAsyncTask task = null;
    Context context;
    private EditText userName, userEmail, password, rePassword;
    private CheckBox nonSvnitian;
    private Button nextButton;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String userNameData = null;
    private String userPasswordData = null;
    private LinearLayout mRoot;
    private View.OnClickListener mSnackBarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_reg_layout);
        userName = (EditText) findViewById(R.id.user_name_edit_text);
        userEmail = (EditText) findViewById(R.id.email_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        rePassword = (EditText) findViewById(R.id.retype_password_edit_text);
        nonSvnitian = (CheckBox) findViewById(R.id.non_svnitian_check_box);
        nextButton = (Button) findViewById(R.id.next_step_button);
        mRoot = (LinearLayout) findViewById(R.id.root_layout);
        nextButton.setOnClickListener(this);

        context = this;
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
        task = new HttpAsyncTask();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_step_button) {


            boolean isEmptyEmail = isEmptyEmail();
            boolean isEmptyPassword = isEmptyPassword();
            boolean isEmptyrePassword = isEmptyrePassword();
            boolean isEmptyUserName = isEmptyUserName();
            if (isEmptyPassword || isEmptyrePassword || isEmptyUserName) {
                Snackbar.make(mRoot, "One Or More Fields Are Blank", Snackbar.LENGTH_SHORT)
                        .setAction("Dismiss", mSnackBarClickListener)
                        .show();
            } else {
                if (password.getText().toString().equals(rePassword.getText().toString())) {

                    userNameData = userName.getText().toString();
                    userPasswordData = password.getText().toString();
                    JSONObject jsonObject =
                            RequestorGet.requestJsonData(requestQueue, API.USER_NAME_CHECK_API, userNameData, userPasswordData, context);
//                    task.execute(userNameData,userPasswordData);


                } else {
                    Snackbar.make(mRoot, "Password Does Not Match", Snackbar.LENGTH_SHORT)
                            .setAction("Dismiss", mSnackBarClickListener)
                            .show();
                }

            }
        }
    }

    private boolean isEmptyEmail() {
        return userEmail.getText() == null || userEmail.getText().toString().isEmpty();

    }

    private boolean isEmptyPassword() {
        return password.getText() == null || password.getText().toString().isEmpty();
    }

    private boolean isEmptyrePassword() {
        return rePassword.getText() == null || rePassword.getText().toString().isEmpty();
    }

    private boolean isEmptyUserName() {
        return userName.getText() == null || userName.getText().toString().isEmpty();
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onDataLoaded(boolean response) {

        if (response) {
            Snackbar.make(mRoot, "User Already registered", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss", mSnackBarClickListener)
                    .show();
        } else {
            if (nonSvnitian.isChecked()) {
                startActivity(new Intent(this, NonSvnitRegActivity.class));
            } else {
                startActivity(new Intent(this, SvnitRegActivity.class));
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Void> {
        Dialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SpotsDialog(RegistrationActivity.this);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i("vikas", "AsyncTask");
            JSONObject jsonObject =
                    RequestorGet.requestJsonData(requestQueue, API.USER_NAME_CHECK_API, params[0], params[1], context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            task.cancel(true);
            dialog.dismiss();
        }
    }
}
