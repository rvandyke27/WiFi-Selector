package com.csc214.rvandyke.wifiselector.model;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanResultFilter {
    private static final String TAG = "ScanResultFilter";

    protected WifiManager mWifiManager;
    protected List<AccessPoint> mAPList;
    protected FavoriteAPList mFavoritedAP;
    private String mSSID;

    public ScanResultFilter(WifiManager wifiManager, String SSID, Context c){
        mWifiManager = wifiManager;
        mSSID = SSID;
        mFavoritedAP = FavoriteAPList.get(c.getApplicationContext());

        updateScan();
    } //ScanResultFilter()

    public void updateScan(){
        Log.d(TAG, "updateScan() called");
        mWifiManager.startScan();
    } //updateScan()

    //should only be called after scan is completed
    public void filterScans(List<ScanResult> unfiltered){
        ArrayList<AccessPoint> filtered = new ArrayList<>();
        Log.d(TAG, "filtering for SSID " + mSSID);
        for (ScanResult s : unfiltered) {
            //Log.d(TAG, "AP SSID= " + s.SSID);
            if (s.SSID.equals(mSSID)) {
                AccessPoint temp = new AccessPoint(s);
                Log.d(TAG, "found access point " + s.BSSID);
                if (mFavoritedAP.contains(s.BSSID)) {
                    temp.setFavorited(true);
                }
                filtered.add(temp);
            }
        }
        Collections.sort(filtered);
        mAPList = filtered;
    }

    public List<AccessPoint> getAccessPoints(){
        return mAPList;
    } //getAccessPoints()

    public int getAPSignalStrength(String BSSID, WifiManager wifiManager){
        int RSSI = -100;
        for(AccessPoint s: mAPList){
            if(s.getBSSID().equals(BSSID)){
                RSSI = s.getSignalLevel();
            }
        }
        return RSSI;
    } //getAPSignalStrength


} //end class
