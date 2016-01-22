package com.sophomoreventure.collegeconnect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Activities.LoginActivity;
import com.sophomoreventure.collegeconnect.Network.DataListener;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Murali on 22/01/2016.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, DataListener {
    EditText emailEditText;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        emailEditText = (EditText) findViewById(R.id.forgotEmailEditText);
        resetPasswordButton.setOnClickListener(this);
        volleySingleton = new VolleySingleton(this);
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.resetPasswordButton) {
            if (isEmailValid(emailEditText.getText())) {
                try {
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
    public void onDataLoaded(boolean response) {
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
    public void setError(String errorCode) {
        emailEditText.setError(errorCode);

    }

    private JSONObject getJsonBody(String emailAddress) throws JSONException {
        return new JSONObject().put("email", emailAddress);
    }
}
