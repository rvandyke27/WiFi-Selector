package com.csc214.rvandyke.wifiselector;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public abstract class MenuActivity extends AppCompatActivity implements WifiErrorDialog.WifiConfiguredListener{
    private static final String TAG = "MenuActivity";

    protected WifiManager mWifiManager;
    protected String mSSID;
    protected String mBSSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager manager = (ConnectivityManager)getSystemService(MenuActivity.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()){
            mSSID = mWifiManager.getConnectionInfo().getSSID().replaceAll("^\"(.*)\"$", "$1");
            mBSSID = mWifiManager.getConnectionInfo().getBSSID().replaceAll("^\"(.*)\"$", "$1");
        }
        else{
            WifiErrorDialog dialog = new WifiErrorDialog();
            dialog.show(getSupportFragmentManager(), "wifi");
        }

    } //onCreate()

    public void onWifiConfigured(){
        this.recreate();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.application_menu, menu);
        return true;
    } //onCreateOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                handled = true;
                break;
            case R.id.menu_item_help:
                Log.d(TAG, "Help activity launched from Menu");
                Intent help = new Intent(this, HelpActivity.class);
                help.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(help);
                handled = true;
                break;
            case R.id.menu_item_favorites:
                Log.d(TAG, "Favorites list activity launched from Menu");
                Intent viewFavorites = new Intent(this, FavoriteAPListActivity.class);
                viewFavorites.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(viewFavorites);
                handled = true;
                break;
            case R.id.menu_item_current:
                Log.d(TAG, "Current Connection test activity launched from Menu");
                Intent currentConnection = new Intent(this, CurrentConnectionInfoActivity.class);
                currentConnection.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(currentConnection);
                handled = true;
                break;
            case R.id.menu_item_map:
                Log.d(TAG, "Map activity launched from Menu");
                Intent advancedReq = new Intent(this, MyMapActivity.class);
                advancedReq.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(advancedReq);
                handled = true;
                break;
            case R.id.menu_item_dog:
                Log.d(TAG, "Dog activity launched from Menu");
                Intent dog = new Intent(this, DogActivity.class);
                dog.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(dog);
                handled = true;
                break;
            default:
                handled = super.onOptionsItemSelected(item);
                break;
        }
        return handled;
    } //onOptionsItemSelected()

} //end class MenuActivity
