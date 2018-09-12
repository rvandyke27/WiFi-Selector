package com.csc214.rvandyke.wifiselector;


import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.csc214.rvandyke.wifiselector.sound.SoundByte;
import com.csc214.rvandyke.wifiselector.sound.SoundLoader;

import java.io.IOException;
import java.util.ArrayList;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class DogFragment extends Fragment {
    private static final String TAG = "DogFragment";

    private MediaPlayer mPlayer;
    private AssetManager mAssets;
    private SoundLoader mSoundLoader;
    private ArrayList<SoundByte> mSounds;
    private int mTrackPosition;

    private final int[] mButtonList = {R.id.button_bark1, R.id.button_bark2, R.id.button_bark3, R.id.button_bark4, R.id.button_bark5, R.id.button_bark6};

    public DogFragment() {
        // Required empty public constructor
    }

    public static DogFragment newInstance(){
        return new DogFragment();
    } //newInstance()

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "onCreate() called");

        mPlayer = new MediaPlayer();

        mAssets = getActivity().getAssets();

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "Media is prepared, starting audio");
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });

        try {
            AssetFileDescriptor afd = mAssets.openFd("audio/mus_dogsong.ogg");
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mPlayer.prepare();
        }
        catch(IOException e) {
            Log.e(TAG, "file not found");
        }

        mSoundLoader = new SoundLoader(getContext());
        mSounds = (ArrayList)mSoundLoader.getSounds();
    } //onCreate()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dog, container, false);

        for(int i =1; i < 7; i++){
            Button barkButton = (Button)view.findViewById(mButtonList[i-1]);
            final SoundByte sound = mSounds.get(i-1);
            barkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSoundLoader.play(sound);
                    Toast.makeText(getContext(), "Bark!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    } //onCreateview()

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPlayer.release();
        mPlayer = null;
        Log.d(TAG, "onDestroy called");
    } //onDestroy()

    @Override
    public void onPause(){
        super.onPause();
        if(mPlayer != null) {
            mPlayer.pause();
            mTrackPosition = mPlayer.getCurrentPosition();
        }
    } //onPause()

    @Override
    public void onResume(){
        super.onResume();
        if(mPlayer != null){
            mPlayer.seekTo(mTrackPosition);
            mPlayer.start();
        }
    } //onResume()


} //end class DogFragment
