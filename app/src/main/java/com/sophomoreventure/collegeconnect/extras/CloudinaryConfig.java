package com.sophomoreventure.collegeconnect.extras;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Murali on 04/02/2016.
 */
public class CloudinaryConfig {
    private static Map config;

    public static Map getConfig() {
        if (config == null) {
            config = new HashMap();
            config.put("cloud_name", "college-connect11");
            config.put("api_key", "161613715213917");
            config.put("api_secret", "T53LI6d-JSuFNOMny4llvzGhCJU");
        }
        return config;
    }
}
