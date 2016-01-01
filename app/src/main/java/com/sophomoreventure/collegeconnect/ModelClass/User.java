package com.sophomoreventure.collegeconnect.ModelClass;

/**
 * Created by Vikas Kumar on 01-01-2016.
 */
public class User {
    public String Email;
    public String userName;
    public String password;
    public String token;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
