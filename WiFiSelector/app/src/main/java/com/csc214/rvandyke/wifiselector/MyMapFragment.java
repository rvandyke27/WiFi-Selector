package com.csc214.rvandyke.wifiselector;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class MyMapFragment extends SupportMapFragment {
    private static final String TAG  = "MyMapFragment";

    public static final float ZOOM = 16.0f;

    private GoogleApiClient mClient;
    private GoogleMap mMap;

    public MyMapFragment() {
        // Required empty public constructor
    }

    public static MyMapFragment newInstance(){
        return new MyMapFragment();
    } //newInstance()

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle){
                        Log.d(TAG, "Google API Client connected");
                        updateLocation();
                    }
                    @Override
                    public void onConnectionSuspended(int i){

                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "Map ready");
                mMap = googleMap;
                if(mClient.isConnected()){
                    updateLocation();
                }
            }
        });

    } //onCreate()

    public void updateLocation(){
        Log.d(TAG, "updateLocation called");
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if(mMap!=null){
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions locationMarker = new MarkerOptions().position(currentLocation);

                            mMap.clear();
                            mMap.addMarker(locationMarker);

                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM);
                            mMap.animateCamera(update);
                        }
                    }
                });
    } //updateLocation()

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        mClient.connect();
    } //onStart()

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
        mClient.disconnect();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    } //onDestroy()

} //end class AdvancedRequirementsFragment
