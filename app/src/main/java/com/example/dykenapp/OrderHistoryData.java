package com.example.dykenapp;

public class OrderHistoryData {

    String mDate, mTotalPrice;

    public OrderHistoryData() {
    }

    public OrderHistoryData(String mDate, String mTotalPrice) {
        this.mDate = mDate;
        this.mTotalPrice = mTotalPrice;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTotalPrice() {
        return mTotalPrice;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public void setmTotalPrice(String mTotalPrice) {
        this.mTotalPrice = mTotalPrice;
    }
}
