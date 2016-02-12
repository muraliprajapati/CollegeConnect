package com.sophomoreventure.collegeconnect;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Activities.LoginActivity;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.extras.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 22/01/2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, DataListener {
    AutoCompleteTextView emailEditText;
    Dialog spotsDialog;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        emailEditText = (AutoCompleteTextView) findViewById(R.id.forgotEmailEditText);
        emailEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getAccountEmailAddress(this)));
        resetPasswordButton.setOnClickListener(this);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
        spotsDialog = new SpotsDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resetPasswordButton) {
            if (isEmailValid(emailEditText.getText())) {
                try {
                    spotsDialog.show();
                    RequestorPost.requestForgotPassword(requestQueue, API.USER_FORGOT_PASSWORD_API, getJsonBody(emailEditText.getText().toString()), this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                emailEditText.setError("Email id is not valid");
            }
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onDataLoaded(String url) {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();
        }
        new AlertDialog.Builder(this)
                .setMessage("An Email has been sent to your email id with password reset link. Please check your email")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                })
                .show();

    }

    @Override
    public void setError(String apiUrl, String errorCode) {
        if (spotsDialog.isShowing()) {
            spotsDialog.dismiss();
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
        } else {
            emailEditText.setError(errorCode);
        }


    }

    private JSONObject getJsonBody(String emailAddress) throws JSONException {
        return new JSONObject().put("email", emailAddress);
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
