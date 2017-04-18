package com.example.ashish.internchat;

/**
 * Created by ashis on 4/15/2017.
 */

public class UserDetail {

    public String mUserName;
    public String mPassword;
    public String mChatWith;
    public String mUserUid;

    public UserDetail() {

    }

    public String getmUserUid() {
        return mUserUid;
    }

    public void setmUserUid(String mUserUid) {
        this.mUserUid = mUserUid;
    }

    public UserDetail(String mUserName, String mPassword, String mUserUid) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.mUserUid = mUserUid;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmChatWith(String mChatWith) {
        this.mChatWith = mChatWith;
    }

    public String getmChatWith() {
        return mChatWith;
    }
}
