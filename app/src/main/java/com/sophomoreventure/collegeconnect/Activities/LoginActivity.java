package com.sophomoreventure.collegeconnect.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.ForgotPasswordActivity;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.extras.API;

import java.util.ArrayList;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 30/12/2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DataListener, View.OnKeyListener {
    AutoCompleteTextView emailEditText;
    EditText passEditText;

    TextView forgotPasswordTextView;
    Button loginButton;
    Button regButton;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private Context context;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            emailEditText.setText(savedInstanceState.getString("email", null));
            passEditText.setText(savedInstanceState.getString("password", null));
        }
        setContentView(R.layout.activity_login);
        emailEditText = (AutoCompleteTextView) findViewById(R.id.input_email);
        passEditText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.login_button);
        regButton = (Button) findViewById(R.id.register_button);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgot_password_text_view);
        dialog = new SpotsDialog(this, R.style.Login_dialog);
        dialog.setCanceledOnTouchOutside(false);
        context = this;
        emailEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getAccountEmailAddress(this)));
        passEditText.setOnKeyListener(this);
        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();

        regButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        forgotPasswordTextView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        final String userName = emailEditText.getText().toString();
        final String password = EventUtility.getHashString(passEditText.getText().toString(), "SHA-1");

        int id = v.getId();
        switch (id) {
            case R.id.login_button:
                if (isEmptyUserName(userName) && isEmptyPassword(password)) {
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

                    emailEditText.clearFocus();
                    passEditText.clearFocus();
                    dialog.show();
                    Log.i("tag", password);
                    RequestorGet.requestUserInfo(requestQueue, API.USER_PROFILE_API, userName, password, LoginActivity.this);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RequestorGet.requestLogin(requestQueue, API.USER_LOGIN_API, userName, password, LoginActivity.this);
                        }
                    }, 500);

                }

                break;

            case R.id.register_button:
                startActivity(new Intent(v.getContext(), RegistrationActivity.class));
                break;

            case R.id.forgot_password_text_view:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

        }
    }

    private boolean isEmptyUserName(String userName) {

        return TextUtils.isEmpty(userName);
    }

    private boolean isEmptyPassword(String userPassword) {

        return TextUtils.isEmpty(userPassword);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("email", emailEditText.getText().toString());
        outState.putString("password", passEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDataLoaded(String apiUrl) {
        if (apiUrl.equalsIgnoreCase(API.USER_PROFILE_API)) {
            Log.i("vikas", "in onDataLoded");
            dialog.dismiss();
        } else if (apiUrl.equalsIgnoreCase(API.USER_LOGIN_API)) {
            Intent intent = new Intent(context, SlideShowActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
            dialog.dismiss();
        }

    }

    @Override
    public void setError(String apiUrl, String errorCode) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        switch (apiUrl) {
            case API.USER_LOGIN_API:
                emailEditText.setError(null);
                passEditText.setError(null);
                Log.i("vikas", "in setError");
                if (errorCode.equals("ERR04")) {
                    emailEditText.setError(EventUtility.getErrorString(errorCode));
                    emailEditText.requestFocus();
                }
                if (errorCode.equals("ERR05")) {
                    passEditText.setError(EventUtility.getErrorString(errorCode));
                    passEditText.requestFocus();
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
                if (errorCode.equals("SERVERERR")) {
                    Toast.makeText(LoginActivity.this, "Server error occured. Please try again",
                            Toast.LENGTH_SHORT).show();
                }
                break;

        }



    }

    private ArrayList<String> getAccountEmailAddress(Context context) {
        ArrayList<String> emailAddressList = new ArrayList<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                emailAddressList.add(possibleEmail);
            }
        }
        return emailAddressList;
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    onClick(loginButton);
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

}
