package com.csc214.rvandyke.wifiselector.sound;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class SoundByte {
    private final String mPath;
    private final String mTitle;
    private Integer mId;

    public SoundByte(String path, String title) {
        mPath = path;
        mTitle = title;
    }

    public String getPath(){
        return mPath;
    }

    public Integer getId(){
        return mId;
    }

    public void setId(Integer id){
        mId = id;
    }

} //end class SoundByte
