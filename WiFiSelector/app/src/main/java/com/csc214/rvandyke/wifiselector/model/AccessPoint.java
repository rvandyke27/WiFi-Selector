package com.csc214.rvandyke.wifiselector.model;

import android.net.wifi.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class AccessPoint implements Comparable<AccessPoint>{
    private static final String TAG = "AccessPoint";

    private String mBSSID;
    private String mSSID;
    private String mNickname;
    private int mSignalLevel;
    private String mNotes;
    private boolean mFavorited;

    public AccessPoint(ScanResult scan){
        mBSSID = scan.BSSID;
        mSSID = scan.SSID;
        if(Build.VERSION.SDK_INT >= 23) {
            mNickname = scan.operatorFriendlyName.toString();
        }
        else{
            mNickname = mBSSID;
        }
        mSignalLevel = scan.level;
        mFavorited = false;
        mNotes = "";
    } //AccessPoint()

    public AccessPoint(String bssid, String ssid, String nickname, String notes){
        mBSSID = bssid;
        mSSID = ssid;
        mNickname = nickname;
        mNotes = notes;
        mSignalLevel = -100;
    } //AccessPoint()

    @Override
    public int compareTo(AccessPoint other){
        return other.getSignalLevel() - mSignalLevel;
    } //compareTo()

    public boolean isFavorited(){
        return mFavorited;
    }

    public void setFavorited(boolean favorited){
        mFavorited = favorited;
    }

    public String getBSSID(){
        return mBSSID;
    } //getBSSID

    public String getSSID(){
        return mSSID;
    } //getSSID()

    public String getNickname(){
        return mNickname;
    } //getNickname()

    public String getNotes(){
        return mNotes;
    } //getNotes()

    public int getSignalLevel(){
        return mSignalLevel;
    } //getSignalLevel()

    public void setSignalLevel(int signalLevel){
        mSignalLevel = signalLevel;
    }

    public void setNickname(String nickname){
        mNickname = nickname;
    } //setNickname()

    public void setNotes(String notes){
        mNotes = notes;
    } //setNotes()

} //end class AccessPoint
