package com.csc214.rvandyke.wifiselector.database;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class FavoriteAPsDBSchema {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "social_network.db";

    public static final class APTable {
        public static final String NAME = "access_points";

        public static final class Cols{
            public static final String BSSID = "bssid";
            public static final String SSID = "ssid";
            public static final String NICKNAME = "nickname";
            public static final String NOTES = "notes";
        } //end class Cols
    } //end class APTable
} //end class FavoriteNetworksDbSchema
