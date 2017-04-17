package com.example.ashish.internchat;

import java.util.Date;

/**
 * Created by ashis on 4/17/2017.
 */

public class GroupDetail {

    public String mGroupName;
    public long mGroupCreatnTime;

    public GroupDetail(){}

    public GroupDetail(String mGroupName) {
        this.mGroupName = mGroupName;
        this.mGroupCreatnTime = new Date().getTime();
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public void setmGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public long getmGroupCreatnTime() {
        return mGroupCreatnTime;
    }

    public void setmGroupCreatnTime(long mGroupCreatnTime) {
        this.mGroupCreatnTime = mGroupCreatnTime;
    }
}
