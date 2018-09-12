package com.csc214.rvandyke.wifiselector.model;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.csc214.rvandyke.wifiselector.database.FavoriteAPsCursorWrapper;
import com.csc214.rvandyke.wifiselector.database.FavoriteAPsDBSchema;
import com.csc214.rvandyke.wifiselector.database.FavoriteAPsDatabaseHelper;
import com.csc214.rvandyke.wifiselector.database.FavoriteAPsDBSchema.APTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteAPList {
    private static final String TAG = "FavoriteAPList";

    private static FavoriteAPList sFavoriteAPList;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final List<AccessPoint> mAPList;

    private FavoriteAPList(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new FavoriteAPsDatabaseHelper(mContext).getWritableDatabase();
        mAPList = new ArrayList<>();
    } //FavoriteAPList()

    public static synchronized FavoriteAPList get(Context c) {
        if(sFavoriteAPList == null){
            sFavoriteAPList = new FavoriteAPList(c);
        }
        return sFavoriteAPList;
    } //get()

    public List<AccessPoint> getFavoritedAPs(){
        mAPList.clear();
        FavoriteAPsCursorWrapper wrapper = queryAPs(null, null);

        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()) {
                AccessPoint ap = wrapper.getAccessPoint();
                mAPList.add(ap);
                wrapper.moveToNext();
            }
        }
        finally{
            wrapper.close();
        }
        Collections.sort(mAPList);
        return mAPList;
    } //getFavoritedAPs()

    public boolean contains(String BSSID){
        return getAccessPoint(BSSID)!=null;
    } //contains

    public void addFavorite(AccessPoint ap) {
        ContentValues values = getContentValues(ap);
        mDatabase.insert(APTable.NAME, null, values);
        Log.d(TAG, "added " + ap.getNickname() + " to favorites");
    } //addFavorite()

    public boolean removeFavorite(String BSSID){
        Log.d(TAG, "unfavorite " + BSSID);
        return mDatabase.delete(APTable.NAME,
                APTable.Cols.BSSID + "=?",
                new String[]{BSSID}) > 0;
    } //removeFavorite()

    public AccessPoint getAccessPoint(String BSSID) {
        FavoriteAPsCursorWrapper wrapper = queryAPs(null, null);
        AccessPoint result = null;

        try{
            wrapper.moveToFirst();
            while(!wrapper.isAfterLast()){
                AccessPoint ap = wrapper.getAccessPoint();
                if(ap.getBSSID().equals(BSSID)){
                    result = ap;
                    break;
                }
                wrapper.moveToNext();
            }
        }
        finally{
            wrapper.close();
        }
        return result;
    } //getAccessPoint()

    public void updateAP(AccessPoint ap) {
        String bssid = ap.getBSSID();
        ContentValues values = getContentValues(ap);
        Log.d(TAG, "updated " + ap.getNickname() + " in database");
        mDatabase.update(APTable.NAME,
                values,
                APTable.Cols.BSSID + "=?",
                new String[]{bssid});
    } //updateAP()

    private FavoriteAPsCursorWrapper queryAPs(String where, String[] args) {
        Cursor cursor = mDatabase.query(
                FavoriteAPsDBSchema.APTable.NAME,
                null,
                where,
                args,
                null,
                null,
                null
        );
        return new FavoriteAPsCursorWrapper(cursor);
    } //queryAPS()

    private static ContentValues getContentValues(AccessPoint ap){
        ContentValues values = new ContentValues();
        values.put(APTable.Cols.BSSID, ap.getBSSID());
        values.put(APTable.Cols.SSID, ap.getSSID());
        values.put(APTable.Cols.NICKNAME, ap.getNickname());
        values.put(APTable.Cols.NOTES, ap.getNotes());

        return values;
    }

} //end class FavoriteApList
