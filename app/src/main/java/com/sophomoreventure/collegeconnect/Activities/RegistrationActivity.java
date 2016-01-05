package com.sophomoreventure.collegeconnect.Activities;

import android.content.Context;
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
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener,DataListener{

    HttpAsyncTask task = null;
    Context context;
    private EditText userName,userEmail,password,rePassword;
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
        if(v.getId() == R.id.next_step_button){


            boolean isEmptyEmail = isEmptyEmail();
            boolean isEmptyPassword = isEmptyPassword();
            boolean isEmptyrePassword = isEmptyrePassword();
            boolean isEmptyUserName = isEmptyUserName();
            if (isEmptyEmail || isEmptyPassword || isEmptyrePassword ||isEmptyUserName) {
                Snackbar.make(mRoot, "One Or More Fields Are Blank", Snackbar.LENGTH_SHORT)
                        .setAction("Dismiss", mSnackBarClickListener)
                        .show();
            } else {
                if(password.getText().toString().equals(rePassword.getText().toString())){
                    if(isEmailValid(userEmail.getText().toString())){
                        userNameData = userName.getText().toString();
                        userPasswordData = password.getText().toString();
                        task.execute();
                    }else {
                        Snackbar.make(mRoot, "Invalid Email Address", Snackbar.LENGTH_LONG)
                                .show();
                    }

                }else {
                    Snackbar.make(mRoot, "Password Does Not Match", Snackbar.LENGTH_LONG)
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

        if(response) {
            Snackbar.make(mRoot, "User Already registered", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss", mSnackBarClickListener)
                    .show();
        }else{
            if(nonSvnitian.isChecked()){
                setContentView(R.layout.second_svnit_reg_layout);
            }else {
                setContentView(R.layout.second_non_svnit_reg_layout);
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... urls) {
            Log.i("vikas","AsyncTask");
            JSONObject jsonObject =
                    RequestorPost.requestJsonData(requestQueue, API.USER_REG_API, userNameData, userPasswordData,context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //task.cancel(true);
        }
    }
}
