package com.sophomoreventure.collegeconnect.ModelClass;

/**
 * Created by Vikas Kumar on 01-02-2016.
 */
public class ClubModel {

    private String clubName;
    private String clubID;
    private String clubDescription;
    private String clubHead;
    private String clubHeadEmail;
    private String clubHeadMob;
    private String ImageUrl;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getClubHeadEmail() {
        return clubHeadEmail;
    }

    public void setClubHeadEmail(String clubHeadEmail) {
        this.clubHeadEmail = clubHeadEmail;
    }

    public String getClubHeadMob() {
        return clubHeadMob;
    }

    public void setClubHeadMob(String clubHeadMob) {
        this.clubHeadMob = clubHeadMob;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubID() {
        return clubID;
    }

    public void setClubID(String clubID) {
        this.clubID = clubID;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getClubHead() {
        return clubHead;
    }

    public void setClubHead(String clubHead) {
        this.clubHead = clubHead;
    }
}
