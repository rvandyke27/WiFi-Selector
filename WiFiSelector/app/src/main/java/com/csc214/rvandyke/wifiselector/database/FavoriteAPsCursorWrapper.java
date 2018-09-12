package com.csc214.rvandyke.wifiselector.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.csc214.rvandyke.wifiselector.model.AccessPoint;
import com.csc214.rvandyke.wifiselector.database.FavoriteAPsDBSchema.APTable;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class FavoriteAPsCursorWrapper extends CursorWrapper {

    public FavoriteAPsCursorWrapper(Cursor cursor) {
        super(cursor);
    } //FavoriteAPsCursorWrapper()

    public AccessPoint getAccessPoint() {
        String bssid = getString(getColumnIndex(APTable.Cols.BSSID));
        String ssid = getString(getColumnIndex(APTable.Cols.SSID));
        String nickname = getString(getColumnIndex(APTable.Cols.NICKNAME));
        String notes = getString(getColumnIndex(APTable.Cols.NOTES));

        AccessPoint ap = new AccessPoint(bssid, ssid, nickname, notes);

        return ap;
    } //getAccessPoint()

} //end class FavoriteApsCursorWrapper
