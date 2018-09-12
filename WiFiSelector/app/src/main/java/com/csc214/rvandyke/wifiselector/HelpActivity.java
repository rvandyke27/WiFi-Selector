package com.csc214.rvandyke.wifiselector;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class HelpActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Log.d("Help Activity", "onCreate() called");

        ImageView ap = (ImageView)findViewById(R.id.image_view_ap);
        Resources res = getResources();
        Drawable accessPoint = res.getDrawable(R.drawable.wifiap);
        ap.setImageDrawable(accessPoint);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Help");
    } //onCreate()
} //end class HelpActivity
