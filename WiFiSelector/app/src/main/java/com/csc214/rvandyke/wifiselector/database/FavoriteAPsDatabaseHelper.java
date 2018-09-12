package com.csc214.rvandyke.wifiselector.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.csc214.rvandyke.wifiselector.database.FavoriteAPsDBSchema.APTable;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class FavoriteAPsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "FavoriteApsDBHelper";

    public FavoriteAPsDatabaseHelper(Context context) {
        super(context, FavoriteAPsDBSchema.DATABASE_NAME, null, FavoriteAPsDBSchema.VERSION);
        Log.d(TAG, "database helper created");
    } //FavoriteAPsDatabaseHelper

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + APTable.NAME
            + "(_id integer primary key autoincrement, "
            + APTable.Cols.BSSID + ", "
            + APTable.Cols.SSID + ", "
            + APTable.Cols.NICKNAME + ", "
            + APTable.Cols.NOTES + ")");
    } //onCreate()

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    } //onUpgrade()

} //end class FavoriteApsDatabaseHelper
