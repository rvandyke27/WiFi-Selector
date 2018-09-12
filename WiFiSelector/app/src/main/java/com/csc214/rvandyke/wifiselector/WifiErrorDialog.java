package com.csc214.rvandyke.wifiselector;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class WifiErrorDialog extends DialogFragment {

    public interface WifiConfiguredListener{
        public void onWifiConfigured();
    }

    private WifiConfiguredListener mListener;

    public WifiErrorDialog() {
        // Required empty public constructor
    }

    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (WifiConfiguredListener) context;
        }
        catch( ClassCastException e){
            Log.e("WifiErrorDialog", "Parent activity must implement wificonfigured listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_wifi_error_dialog, null);

        Button configureWifi = (Button)view.findViewById(R.id.button_configure_wifi);
        configureWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });

        return new AlertDialog.Builder(getContext()).setView(view).create();
    } //onCreateDialog()

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(MenuActivity.CONNECTIVITY_SERVICE);
            if(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()){
                mListener.onWifiConfigured();
                dismiss();
            }
        }
    } //onActivityResult()

} //end class WifiErrorDialog
