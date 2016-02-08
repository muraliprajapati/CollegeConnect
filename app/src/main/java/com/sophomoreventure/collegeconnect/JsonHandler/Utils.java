package com.sophomoreventure.collegeconnect.JsonHandler;

import org.json.JSONObject;

/**
 * Created by Vikas Kumar on 06-02-2016.
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

}
