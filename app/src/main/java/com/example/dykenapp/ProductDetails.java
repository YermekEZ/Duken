package com.example.dykenapp;

public class ProductDetails {

    String mProductName, mSuccess;

    public ProductDetails() {

    }

    public ProductDetails(String mProductName, String mSuccess) {
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
