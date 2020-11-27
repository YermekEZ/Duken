package com.example.dykenapp;

public class AddStoreData {

    private String mPhoneNumber, mIIN, mLegalName, mCity, mAddress;

    public AddStoreData() {

    }

    public AddStoreData(String mPhoneNumber, String mIIN, String mLegalName, String mCity, String mAddress) {
        this.mPhoneNumber = mPhoneNumber;
        this.mIIN = mIIN;
        this.mLegalName = mLegalName;
        this.mCity = mCity;
        this.mAddress = mAddress;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmIIN() {
        return mIIN;
    }

    public void setmIIN(String mIIN){
        this.mIIN = mIIN;
    }

    public String getmLegalName() {
        return mLegalName;
    }

    public void setmLegalName(String mLegalName) {
        this.mLegalName = mLegalName;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
