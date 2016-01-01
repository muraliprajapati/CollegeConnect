package com.sophomoreventure.collegeconnect.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.R;
import com.sophomoreventure.collegeconnect.UserInfoTask;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Murali on 30/12/2015.
 */
public class LoginActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passEditText;
    Button button;
    Button regButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.input_email);
        passEditText = (EditText) findViewById(R.id.input_password);
        button = (Button) findViewById(R.id.login_button);
        regButton = (Button) findViewById(R.id.register_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoTask userInfoTask = new UserInfoTask();
                userInfoTask.execute();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpAsyncTask task = new HttpAsyncTask();
                task.execute();
            }
        });

    }

    private class HttpAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... urls) {


            String urlString = API.USER_REG_API;
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
