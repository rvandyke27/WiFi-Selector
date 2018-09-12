package com.csc214.rvandyke.wifiselector.sound;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.csc214.rvandyke.wifiselector.sound.SoundByte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundLoader {
    private static final String TAG = "SoundLoader";

    private AssetManager mAssets;
    private List<SoundByte> mSounds;
    private SoundPool mSoundPool;

    public SoundLoader(Context context) {
        mAssets = context.getAssets();
        mSounds = new ArrayList<>();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        String[] trackNames;
        try{
            trackNames = mAssets.list("audio/bark");
            int i = 1;
            for(String filename: trackNames) {
                Log.d(TAG, "loaded track " + filename);
                String path = "audio/bark/" + filename;
                SoundByte sound = new SoundByte(path, "Bark " + i);
                mSounds.add(sound);

                try{
                    AssetFileDescriptor afd = mAssets.openFd(sound.getPath());
                    int soundId = mSoundPool.load(afd, 1);
                    sound.setId(soundId);
                }
                catch(IOException e){
                    Log.e(TAG, "load sound file failed");
                }
                i++;
            }
        }
        catch(IOException e){
            Log.e(TAG, "Sound load failed");
        }

        Log.d(TAG, "loaded " + mSounds.size() + " sound files");
    } //SoundLoader()

    public void play(SoundByte sound){
        Integer id = sound.getId();
        if(id != null){
            mSoundPool.play(
                    id,
                    0.8f,
                    0.8f,
                    1,
                    0,
                    1.0f
            );
        }
    } //play()

    public List<SoundByte> getSounds(){
        return mSounds;
    } //getSounds()

    public void release(){
        mSoundPool.release();
    } //release()

} //end class SoundLoader
