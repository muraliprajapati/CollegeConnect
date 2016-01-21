package com.sophomoreventure.collegeconnect.Network;

/**
 * Created by Vikas Kumar on 30-12-2015.
 */
public interface DataListener {
    public void onDataLoaded(boolean response);

    void setError(String errorCode);
}
