package com.csc214.rvandyke.wifiselector;


import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csc214.rvandyke.wifiselector.model.AccessPoint;
import com.csc214.rvandyke.wifiselector.model.FavoriteAPList;
import com.csc214.rvandyke.wifiselector.model.ScanResultFilter;

import java.util.List;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class ScanResultFragment extends Fragment {
    private static final String TAG = "ScanResultFragment";

    private static String ARG_SSID = "ssid";
    private static String ARG_BSSID = "bssid";

    private RecyclerView mRecyclerView;
    protected String mSSID;
    protected String mBSSID;
    private ScanResultAdapter mAdapter;
    private ScanResultFilter mScanFilter;
    private ScanReceiver mScanReceiver;
    private WifiStateChangedReceiver mWifiStateReceiver;
    protected WifiManager mWifiManager;
    private WifiConfiguration mActiveConfiguration;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mReceiverRegistered;

    public ScanResultFragment() {
        // Required empty public constructor
    }

    public static ScanResultFragment newInstance(String ssid, String bssid){
        Bundle args = new Bundle();
        args.putString(ARG_SSID, ssid);
        args.putString(ARG_BSSID, bssid);
        ScanResultFragment fragment = new ScanResultFragment();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance() called");
        return fragment;
    } //newInstance()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        View view = inflater.inflate(R.layout.fragment_scan_result, container, false);

        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager.isWifiEnabled()) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.scan_result_recycler);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            mSwipeRefreshLayout.setRefreshing(true);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mScanFilter.updateScan();
                }
            });

            Bundle args = getArguments();
            mSSID = args.getString(ARG_SSID);
            mBSSID = args.getString(ARG_BSSID);

            mScanFilter = new ScanResultFilter(mWifiManager, mSSID, getContext());

            for (WifiConfiguration wc : mWifiManager.getConfiguredNetworks()) {
                if (wc.status == WifiConfiguration.Status.CURRENT) {
                    mActiveConfiguration = wc;
                    break;
                }
            }

            mScanReceiver = new ScanReceiver();
            mWifiStateReceiver = new WifiStateChangedReceiver();

            if (Build.VERSION.SDK_INT >= 23) {
                if ((getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) || (getActivity().checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE}, 13);
                    Log.d(TAG, "requesting permissions");
                    mScanFilter.updateScan();
                } else {
                    mScanFilter.updateScan();
                }
            }
        }
        return view;
    } //onCreateview()

    @Override
    public void onResume(){
        super.onResume();
        mScanFilter = new ScanResultFilter((WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE), mSSID, getContext());
        mScanFilter.updateScan();
        IntentFilter i = new IntentFilter();
        i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(mScanReceiver, i);
        IntentFilter j = new IntentFilter();
        j.addAction("android.net.wifi.STATE_CHANGE");
        getActivity().registerReceiver(mWifiStateReceiver, j);
        mReceiverRegistered = true;
        Log.d(TAG, "Receiver registered in onResume()");
    } //onResume

    @Override
    public void onStop(){
        super.onStop();
        if(mReceiverRegistered) {
            try {
                getActivity().unregisterReceiver(mScanReceiver);
                getActivity().unregisterReceiver(mWifiStateReceiver);
                Log.d(TAG, "Receiver unregistered in onPause()");
                mReceiverRegistered = false;
            }
            catch(IllegalArgumentException e){
                Log.d(TAG, "receiver already unregistered");
            }
        }
    } //onStop()

    //NOTE: connectTo will never be called, also won't do anything unless bottom is uncommented
    public boolean connectTo(String bssid){
        WifiConfiguration newWc = new WifiConfiguration();
        //mBSSID = bssid;
        newWc.BSSID = bssid;
        newWc.SSID = mSSID;
        newWc.preSharedKey = mActiveConfiguration.preSharedKey;
        newWc.hiddenSSID = mActiveConfiguration.hiddenSSID;
        newWc.status = WifiConfiguration.Status.ENABLED;
        newWc.allowedAuthAlgorithms = mActiveConfiguration.allowedAuthAlgorithms;
        newWc.allowedGroupCiphers = mActiveConfiguration.allowedGroupCiphers;
        newWc.allowedKeyManagement = mActiveConfiguration.allowedKeyManagement;
        newWc.allowedPairwiseCiphers = mActiveConfiguration.allowedPairwiseCiphers;
        newWc.allowedProtocols = mActiveConfiguration.allowedProtocols;
        newWc.wepKeys = mActiveConfiguration.wepKeys;
        newWc.wepTxKeyIndex = mActiveConfiguration.wepTxKeyIndex;
        if(Build.VERSION.SDK_INT >= 18) {
            newWc.enterpriseConfig = mActiveConfiguration.enterpriseConfig;
        }
        if(Build.VERSION.SDK_INT >= 23) {
            newWc.roamingConsortiumIds = mActiveConfiguration.roamingConsortiumIds;
            newWc.providerFriendlyName = mActiveConfiguration.providerFriendlyName;
        }

        //int netId = mWifiManager.addNetwork(newWc);
        //Log.d(TAG, "Added network w/ id " + netId);
        //mWifiManager.disconnect();
        //return mWifiManager.enableNetwork(netId, true);
        //return mWifiManager.reconnect();

        return false;

    } //connectTo()


    public void dialogDismissed(){
        mScanFilter.updateScan();
        mSwipeRefreshLayout.setRefreshing(true);
    } //onActivityResult()


    private class ScanResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final String TAG = "ScanResultAdapter";
        private List<AccessPoint> mFilteredScanResults;

        public ScanResultAdapter(List<AccessPoint> filteredScanResults){
            mFilteredScanResults = filteredScanResults;
        } //ScanResultAdapter()

        public void updateScans(List<AccessPoint> filteredScanResults) {
            mFilteredScanResults = filteredScanResults;
            notifyDataSetChanged();
        } //update()

        @Override
        public int getItemCount(){
            return mFilteredScanResults.size();
        } //getItemCount()

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            Log.d(TAG, "onCreateViewHolder() called, type=" + viewType);
            switch(viewType){
                case 1: //favorited
                    return new FavoritedAPViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favorited_ap_view_holder, parent, false));
                default: //case 0, not favorited
                    return new ScanResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_result_view_holder, parent, false));
            }
        } //onCreateViewHolder()

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AccessPoint ap = mFilteredScanResults.get(position);
            if(ap.getBSSID().equals(mBSSID)){
                holder.itemView.setBackground(getResources().getDrawable(R.drawable.connected_ap_background));
                Log.d(TAG, "indicate active AP");
            }
            else if(ap.isFavorited()){
                holder.itemView.setBackground(getResources().getDrawable(R.drawable.favorited_ap_background));
            }
            else{
                holder.itemView.setBackground(getResources().getDrawable(R.drawable.nonconnected_ap_background));
            }
            Log.d(TAG, "onBindViewHolder() called on position " + position);
            if(ap.isFavorited()){
                FavoritedAPViewHolder holder1 = (FavoritedAPViewHolder) holder;
                AccessPoint fAP = FavoriteAPList.get(getContext()).getAccessPoint(ap.getBSSID());
                fAP.setSignalLevel(ap.getSignalLevel());
                holder1.bind(fAP);
            }
            else{
                ScanResultViewHolder holder0 = (ScanResultViewHolder) holder;
                holder0.bind(ap);
            }
        } //onBindViewHolder()

        @Override
        public int getItemViewType(int position) {
            if(mFilteredScanResults.get(position).isFavorited()){
                return 1;
            }
            else{
                return 0;
            }
        } //getItemViewType()

    } //end class ScanResultAdapter

    public class FavoritedAPViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FavoritedAPViewHolder";
        private final TextView mNickname;
        private final TextView mRSSI;
        private final TextView mNotes;
        private final Button mConnectButton;
        private final Button mEditButton;

        private AccessPoint  mAccessPoint;

        public FavoritedAPViewHolder(View itemView) {
            super(itemView);
            mNickname = (TextView)itemView.findViewById(R.id.text_view_nickname);
            mRSSI = (TextView)itemView.findViewById(R.id.text_view_signal_strength);
            mNotes = (TextView)itemView.findViewById(R.id.text_view_notes);
            mConnectButton = (Button)itemView.findViewById(R.id.button_connect);
            mEditButton = (Button)itemView.findViewById(R.id.button_edit_entry);

            mConnectButton.setEnabled(false);

            mConnectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!connectTo(mAccessPoint.getBSSID())){
                        Toast.makeText(getContext(), "Connection updated", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Connection failed", Toast.LENGTH_LONG).show();
                    }
                    mScanFilter.updateScan();
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });

            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavoriteDialog addToFavorites = FavoriteDialog.newInstance(mAccessPoint);
                    addToFavorites.show(getFragmentManager(), "favorite");
                }
            });

        } //FavoritedAPViewHolder()

        public void bind(AccessPoint ap){
            Log.d(TAG, "Binding " + ap.getBSSID() + ", Nickname = " + ap.getNickname());
            mAccessPoint = ap;
            mNickname.setText(ap.getNickname());
            String ss = "Signal Strength: " + WifiManager.calculateSignalLevel(ap.getSignalLevel(), 10);
            mRSSI.setText(ss);
            mNotes.setText(ap.getNotes());
        } //bind()

    } //end class

    private class ScanResultViewHolder extends RecyclerView.ViewHolder{
        private final TextView mBSSID;
        private final TextView mRSSI;
        private final Button mConnectButton;
        private final Button mAddToFavoritesButton;
        private AccessPoint mScanResult;

        public ScanResultViewHolder(View itemView){
            super(itemView);
            mBSSID = (TextView)itemView.findViewById(R.id.text_view_bssid);
            mRSSI = (TextView)itemView.findViewById(R.id.text_view_signal_strength);
            mConnectButton = (Button)itemView.findViewById(R.id.button_connect);
            mAddToFavoritesButton = (Button)itemView.findViewById(R.id.button_add_to_favorites);

            mConnectButton.setEnabled(false);

            mConnectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(connectTo(mScanResult.getBSSID())){
                        Toast.makeText(getContext(), R.string.connection_updated, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getContext(), R.string.connection_failed, Toast.LENGTH_LONG).show();
                    }
                    //mScanFilter.updateScan();
                    //mSwipeRefreshLayout.setRefreshing(true);

                }
            });

            mAddToFavoritesButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    FavoriteDialog addToFavorites = FavoriteDialog.newInstance(mScanResult);
                    addToFavorites.show(getFragmentManager(), "favorite");
                }
            });
        } //ScanResultViewHolder()

        public void bind(AccessPoint sr) {
            mScanResult = sr;
            Log.d(TAG, getString(R.string.binding) + sr.getBSSID() + getString(R.string.rssi) + sr.getSignalLevel());
            mBSSID.setText(sr.getBSSID());
            String ss = getString(R.string.signal_strength) + WifiManager.calculateSignalLevel(sr.getSignalLevel(), 10);
            mRSSI.setText(ss);
        } //bind()

    } //end class ScanResultViewHolder

    public class ScanReceiver extends BroadcastReceiver {
        public ScanReceiver(){
        }

        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "ScanReceiver onReceive() called");
            if(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) {
                Log.d(TAG, "onReceive() scan completed");
                List<ScanResult> scanResults = mWifiManager.getScanResults();
                mScanFilter.filterScans(scanResults);
                List<AccessPoint> filteredScans = mScanFilter.getAccessPoints();
                if (mAdapter == null) {
                    mAdapter = new ScanResultAdapter(filteredScans);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.updateScans(filteredScans);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } //onReceive()

    } //end class ScanReceiver

    public class WifiStateChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if(wifi.isConnected()){
                Log.d(TAG, "Network status changed:connected");
                mBSSID = mWifiManager.getConnectionInfo().getBSSID();
                mScanFilter.updateScan();
                mSwipeRefreshLayout.setRefreshing(true);
            }
            else{
                Log.d(TAG, "Network status changed:disconnected");
                mWifiManager.reconnect();
            }
        } //onReceive()
    } //end class WifiStateChangedReceiver

} //end class
