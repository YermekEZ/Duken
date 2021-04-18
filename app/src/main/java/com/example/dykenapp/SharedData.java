package com.example.dykenapp;

public class SharedData {

    static String phoneNumber;
    static String productName, barcode, price, pieces;
    static int maxCount;

    public SharedData(){

    }

    public static String getProductName() {
        return productName;
    }

    public static String getBarcode() {
        return barcode;
    }

    public static String getPrice() {
        return price;
    }

    public static String getPieces() {
        return pieces;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        SharedData.phoneNumber = phoneNumber;
    }

    public static void setProductName(String productName) {
        SharedData.productName = productName;
    }

    public static void setBarcode(String barcode) {
        SharedData.barcode = barcode;
    }

    public static void setPrice(String price) {
        SharedData.price = price;
    }

    public static void setPieces(String pieces) {
        SharedData.pieces = pieces;
    }

    public static int getMaxCount() {
        return maxCount;
    }

    public static void setMaxCount(int maxCount) {
        SharedData.maxCount = maxCount;
    }
}
