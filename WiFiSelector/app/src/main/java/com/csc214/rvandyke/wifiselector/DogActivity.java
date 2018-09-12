package com.csc214.rvandyke.wifiselector;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class DogActivity extends MenuActivity {
    private static final String TAG = "DogActivity";

    private DogFragment mFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);
        Log.d(TAG, "onCreate() called");

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Audio Advanced Requirement");

        FragmentManager manager = getSupportFragmentManager();
        mFrag = (DogFragment) manager.findFragmentById(R.id.dog_frame);

        if(mFrag == null) {
            mFrag = DogFragment.newInstance();
            manager.beginTransaction().add(R.id.dog_frame, mFrag, null).commit();
        }
    } //onCreate()

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    } //onDestroy

} //end class DogActivity
