package com.csc214.rvandyke.wifiselector;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class MyMapActivity extends MenuActivity {
    private static final String TAG = "AdvancedReqActivity";

    private MyMapFragment mFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_my_map);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Map Advanced Requirement");

        FragmentManager manager = getSupportFragmentManager();
        mFrag = (MyMapFragment) manager.findFragmentById(R.id.advanced_requirements_frame);

        if(mFrag == null) {
            mFrag = MyMapFragment.newInstance();
            manager.beginTransaction().add(R.id.advanced_requirements_frame, mFrag, null).commit();
        }
    } //onCreate()

} //end class MyMapActivity
