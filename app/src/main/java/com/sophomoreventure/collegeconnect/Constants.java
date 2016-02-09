package com.sophomoreventure.collegeconnect;

/**
 * Created by Murali on 17/01/2016.
 */
public interface Constants {

    String NA = "NA";

    interface SharedPrefConstants {
        String USER_SHARED_PREF_FILE_NAME = "com.sophomoreventure.collegeconnect.user_pref";
        String USER_SHARED_PREF_USER_NAME_KEY = "user_name";
        String USER_SHARED_PREF_USER_PASSWORD_KEY = "password_hash";
        String USER_SHARED_PREF_USER_TOKEN_KEY = "token";
        String USER_SHARED_PREF_LOGGED_IN_KEY = "logged_in";
        String USER_SHARED_PREF_EMAIL_KEY = "email";
        String USER_SHARED_PREF_MOB_NO_KEY = "mobile_no";
        String USER_SHARED_PREF_ROLL_NO_KEY = "roll_no";
        String USER_SHARED_PREF_HOSTELITE_KEY = "hostelite";
        String USER_SHARED_PREF_USER_VERIFIED_KEY = "verified";
        String USER_SHARED_PREF_HOSTEL_NAME_KEY = "hostel_name";

        String GCM_SHARED_PREF_FILE_NAME = "com.sophomoreventure.collegeconnect.gcm_pref";

        String APP_SHARED_PREF_FILE_NAME = "com.sophomoreventure.collegeconnect.app_pref";
        String APP_SHARED_PREF_VERSION_CODE_KEY = "app_version_code";
        String APP_SHARED_PREF_FIRST_RUN_KEY = "first_run";

    }
}
