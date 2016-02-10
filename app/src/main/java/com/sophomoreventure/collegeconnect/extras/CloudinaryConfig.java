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
            config.put("cloud_name", "dxnadjd73");
            config.put("api_key", "763849438312341");
            config.put("api_secret", "8ueMqS8HtE4tw5dNoHSuC1aJspU");
        }
        return config;
    }
}
