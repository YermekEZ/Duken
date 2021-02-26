package com.example.dykenapp;

public class SharedData {

    static String phoneNumber;

    public SharedData(){

    }

    public SharedData(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        SharedData.phoneNumber = phoneNumber;
    }
}
