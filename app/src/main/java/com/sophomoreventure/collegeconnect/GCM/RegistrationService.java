package com.sophomoreventure.collegeconnect.GCM;

/**
 * Created by Murali on 07/01/2016.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.sophomoreventure.collegeconnect.API;
import com.sophomoreventure.collegeconnect.Constants;
import com.sophomoreventure.collegeconnect.Network.RequestorPost;
import com.sophomoreventure.collegeconnect.Network.VolleySingleton;
import com.sophomoreventure.collegeconnect.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Deal with registration of the user with the GCM instance.
 */
public class RegistrationService extends IntentService {

    private static final String TAG = "tag";
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    public RegistrationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences =
                getSharedPreferences(Constants.SharedPrefConstants.GCM_SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        try {
            // Just in case that onHandleIntent has been triggered several times in short
            // succession.
            volleySingleton = new VolleySingleton(this);
            requestQueue = volleySingleton.getRequestQueue();
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.d(TAG, "GCM registration token: " + token);

                // Register to the server and subscribe to the topic of interest.
                sendRegistrationToServer(token);

                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(RegistrationConstants.SENT_TOKEN_TO_SERVER, true);
                editor.putString(RegistrationConstants.TOKEN, token);
                editor.apply();
            }
        } catch (IOException e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(RegistrationConstants.
                    SENT_TOKEN_TO_SERVER, false).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent registrationComplete = new Intent(RegistrationConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Sends the registration to the server.
     *
     * @param token The token to send.
     * @throws IOException Thrown when a connection issue occurs.
     */
    private void sendRegistrationToServer(String token) throws IOException, JSONException {
        RequestorPost.requestGcmToken(requestQueue, API.GCM_TOKEN_API, createJsonBody(token), this);
//
    }

    private JSONObject createJsonBody(String token) throws JSONException {
        return new JSONObject().put("gcmid", token);
    }


}
