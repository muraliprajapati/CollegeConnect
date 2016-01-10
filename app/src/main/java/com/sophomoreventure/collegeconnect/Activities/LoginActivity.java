package com.sophomoreventure.collegeconnect.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dmax.dialog.SpotsDialog;

/**
 * Created by Murali on 30/12/2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText emailEditText;
    EditText passEditText;
    Button button;
    Button regButton;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String userName = null;
    private String userPassword = null;
    private Context context;
    private TextView circular;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.input_email);
        passEditText = (EditText) findViewById(R.id.input_password);
        button = (Button) findViewById(R.id.login_button);
        regButton = (Button) findViewById(R.id.register_button);
        circular = (TextView) findViewById(R.id.textView);
        context = this;
        //final AlertDialog dialog = new SpotsDialog(this,R.style.Custom_loding_dialogbox);
        final AlertDialog dialog = new SpotsDialog(this);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(),RegistrationActivity.class));
                //UserInfoTask userInfoTask = new UserInfoTask();
                //userInfoTask.execute();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getTag() == "New User"){


            //startActivity(new Intent(v.getContext(),RegistrationActivity.class));
        }
    }


    private class HttpAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... urls) {

            //RequestorPost.requestStringData(requestQueue, API.USER_REG_API, userName, userPassword);

            String urlString = "https://sheltered-fjord-8731.herokuapp.com/api/user/reg";
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);


                String userCredentials = "vikas:password";
                final String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

                connection.setRequestProperty("Authorization", basicAuth);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
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
                    Log.i("tag", jsonResponse);
                    /************** For getting response from HTTP URL end ***************/

                }

            } catch (Exception e) {
                Log.d("tag", e.getLocalizedMessage());
            }
            return null;

        }


    }
}
