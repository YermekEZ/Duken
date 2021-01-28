package com.example.dykenapp;

public class ProductDetails {

    private String barcodeNumber, productName;

    public ProductDetails() {

    }

    public ProductDetails(String barcodeNumber, String productName) {
        this.barcodeNumber = barcodeNumber;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }
}
