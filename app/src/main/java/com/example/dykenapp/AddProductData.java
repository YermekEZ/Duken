package com.example.dykenapp;

public class AddProductData {

    private String mProductName, mSuccess;

    public AddProductData() {

    }

    public AddProductData(String mProductName, String mSuccess) {
        this.mProductName = mProductName;
        this.mSuccess = mSuccess;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }

}
