package com.csc214.rvandyke.wifiselector;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csc214.rvandyke.wifiselector.model.AccessPoint;
import com.csc214.rvandyke.wifiselector.model.FavoriteAPList;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class FavoriteDialog extends DialogFragment {
    private static final String TAG = "FavoriteDialog";

    private static String KEY_NICKNAME = "NICKNAME";
    private static String KEY_SSID = "SSID";
    private static String KEY_BSSID = "BSSID";
    private static String KEY_NOTES = "NOTES";

    private FavoriteAPList mFavoriteAPList;
    private DialogDismissedListener mCallback;

    public interface DialogDismissedListener{
        public void onChildDismissed();
    } //end interface DialogDismissedListener

    public FavoriteDialog(){
        //required empty public constructor
    } //favoriteDialog()

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        try{
            mCallback = (DialogDismissedListener)getActivity();
        }
        catch(ClassCastException e){
            Log.d(TAG, "parent activity must implement DialogDismissedListener");
        }
    } //onCreate()

    public static FavoriteDialog newInstance(AccessPoint ap){
        FavoriteDialog dialog = new FavoriteDialog();

        Bundle args = new Bundle();
        args.putString(KEY_NICKNAME, ap.getNickname());
        args.putString(KEY_SSID, ap.getSSID());
        args.putString(KEY_BSSID, ap.getBSSID());
        args.putString(KEY_NOTES, ap.getNotes());

        dialog.setArguments(args);

        Log.d(TAG, "newInstance() called for " + ap.getNickname());

        return dialog;
    } //newInstance()

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mFavoriteAPList = FavoriteAPList.get(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.favorite_dialog, null);
        final EditText nickname= (EditText)view.findViewById(R.id.edit_text_nickname);
        final TextView ssid = (TextView)view.findViewById(R.id.text_view_ssid);
        final TextView bssid = (TextView)view.findViewById(R.id.text_view_bssid);
        final EditText notes = (EditText)view.findViewById(R.id.edit_text_notes);
        Button cancel = (Button)view.findViewById(R.id.button_cancel);
        Button removeF = (Button)view.findViewById(R.id.button_remove_from_favorites);
        Button ok = (Button)view.findViewById(R.id.button_ok);

        Bundle args = getArguments();

        if(!args.getString(KEY_NICKNAME).equals("")){
            nickname.setText(args.getString(KEY_NICKNAME));
        }
        if(!args.getString(KEY_NOTES).equals("")){
            notes.setText(args.getString(KEY_NOTES));
        }
        ssid.setText(args.getString(KEY_SSID));
        bssid.setText(args.getString(KEY_BSSID));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        removeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoriteAPList.removeFavorite(bssid.getText().toString())){
                    Toast.makeText(getContext(), "Access Point " + bssid.getText().toString() + " unfavorited", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "Deletion failed", Toast.LENGTH_LONG).show();
                }
                done();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoriteAPList.contains(bssid.getText().toString())) {
                    mFavoriteAPList.updateAP(new AccessPoint(bssid.getText().toString(), ssid.getText().toString(), nickname.getText().toString(), notes.getText().toString()));
                }
                else{
                    mFavoriteAPList.addFavorite(new AccessPoint(bssid.getText().toString(), ssid.getText().toString(), nickname.getText().toString(), notes.getText().toString()));
                }
                done();
            }
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .create();
    } //onCreateDialog()

    public void done(){
        mCallback.onChildDismissed();
        dismiss();
    } //onDismiss()

} //end class FavoriteDialog
