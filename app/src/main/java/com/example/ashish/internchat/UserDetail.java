package com.example.ashish.internchat;

/**
 * Created by ashis on 4/15/2017.
 */

public class UserDetail {

    public String mUserName;
    public String mPassword;

    public UserDetail(){

    }

    public UserDetail(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }
}
