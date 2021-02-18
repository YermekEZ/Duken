package com.example.dykenapp;

public class ProductData {

    private String mProductName, mProductDescription;

    public ProductData() {

    }

    public ProductData(String mProductName, String mProductDescription) {
        this.mProductName = mProductName;
        this.mProductDescription = mProductDescription;
    }

    public String getmProductName() {
        return mProductName;
    }

    public void setmProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public String getmProductDescription() {
        return mProductDescription;
    }

    public void setmProductDescription(String mProductDescription) {
        this.mProductDescription = mProductDescription;
    }

}
