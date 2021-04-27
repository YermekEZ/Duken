package com.example.dykenapp;

import java.util.List;

public class OrderData {

    String mName, mPrice, mPieces;

    public OrderData(String mName, String mPieces, String mPrice) {
        this.mName = mName;
        this.mPrice = mPrice;
        this.mPieces = mPieces;
    }

    public String getmPieces() {
        return mPieces;
    }

    public String getmPrice() {
        return mPrice;
    }

    public String getmName() {
        return mName;
    }

    public void setmPieces(String mPieces) {
        this.mPieces = mPieces;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
