package com.sophomoreventure.collegeconnect.Network;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public interface DataListener {
    void onDataLoaded(String apiUrl);

    void setError(String apiUrl, String errorCode);
}
