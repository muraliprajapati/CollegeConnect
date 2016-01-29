package com.sophomoreventure.collegeconnect.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.EventUtility;
import com.sophomoreventure.collegeconnect.ForgotPasswordActivity;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorGet;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 30/12/2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, DataListener {
    AutoCompleteTextView emailEditText;
    EditText passEditText;
    EditText guestNameEditText;
    EditText guestCollegeEditText;
    TextView forgotPasswordTextView;
    Button loginButton;
    Button regButton;
    Button guestLoginButton;
    Button guestFinalLoginButton;
    LinearLayout guestCradLayout;
    boolean isLayoutVisibile = false;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String userName = null;
    private String userPassword = null;
    private Context context;
    private TextView circular;
    private AlertDialog dialog;
    private String loginErrorCode = "empty";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        guestCradLayout = (LinearLayout) findViewById(R.id.guestCradLayout);
        emailEditText = (AutoCompleteTextView) findViewById(R.id.input_email);
        passEditText = (EditText) findViewById(R.id.input_password);
        guestNameEditText = (EditText) findViewById(R.id.guest_name_edit_text);
        guestCollegeEditText = (EditText) findViewById(R.id.guest_college_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);
        regButton = (Button) findViewById(R.id.register_button);
        guestLoginButton = (Button) findViewById(R.id.guest_login_button);
        guestFinalLoginButton = (Button) findViewById(R.id.guest_final_login_button);
        forgotPasswordTextView = (TextView) findViewById(R.id.forgot_password_text_view);
        dialog = new SpotsDialog(this, R.style.Login_dialog);
        dialog.setCanceledOnTouchOutside(false);
        context = this;
        emailEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getAccountEmailAddress(this)));

        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();

        regButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        guestLoginButton.setOnClickListener(this);
        guestFinalLoginButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
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
                    RequestorGet.requestLogin(requestQueue, API.USER_LOGIN_API, userName, password, this);

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
            case R.id.forgot_password_text_view:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDataLoaded(boolean response) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    @Override
    public void setError(String errorCode) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
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
                    .setMessage("It seems your internet is not working working")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
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

}
