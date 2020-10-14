package com.example.dykenapp;

public class AddProfileData {

    private String mName, mSurname, mStateID, mPhoneNumber;

    public AddProfileData() {

    }

    public AddProfileData(String mName, String mSurname, String mStateID, String mPhoneNumber) {
        this.mName = mName;
        this.mSurname = mSurname;
        this.mStateID = mStateID;
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public String getmStateID() {
        return mStateID;
    }

    public void setmStateID(String mStateID) {
        this.mStateID = mStateID;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }
}
