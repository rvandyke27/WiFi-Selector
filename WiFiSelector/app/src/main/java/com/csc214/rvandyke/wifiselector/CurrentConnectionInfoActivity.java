package com.csc214.rvandyke.wifiselector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csc214.rvandyke.wifiselector.model.FavoriteAPList;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/*
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss
 */

public class CurrentConnectionInfoActivity extends MenuActivity {
    private static final String TAG = "CCInfoActivity";

    private static String KEY_SPEED_TEST_RESULT = "speed_test";

    private TextView mSSIDView;
    private TextView mBSSIDView;
    private ImageView mFavoritedStar;
    private TextView mNickname;
    private TextView mSpeedTestResults;
    private SpeedTest mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_connection_info);
        Log.d(TAG, "onCreate() called");

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle("Current Connection");

        mTest = new SpeedTest();

        mSSIDView = (TextView)findViewById(R.id.text_view_ssid);
        mBSSIDView = (TextView)findViewById(R.id.text_view_bssid);
        mFavoritedStar = (ImageView)findViewById(R.id.image_view_favorite_star);
        mNickname = (TextView)findViewById(R.id.text_view_nickname);
        mSpeedTestResults = (TextView)findViewById(R.id.text_view_speed_test_result);

        mSSIDView.setText(mSSID);
        mBSSIDView.setText(mBSSID);
        if(savedInstanceState!=null){
            mSpeedTestResults.setText(savedInstanceState.getCharSequence(KEY_SPEED_TEST_RESULT));
        }

        if(FavoriteAPList.get(this).contains(mBSSID)){
            mFavoritedStar.setImageResource(R.drawable.ic_favorited);
            String nickname = FavoriteAPList.get(this).getAccessPoint(mBSSID).getNickname();
            mNickname.setText(nickname);
            mNickname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }

        Button speedTest = (Button)findViewById(R.id.button_speed_test);
        speedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Speed test requested");
                mTest.execute("https://rochesterymca.org/wp-content/uploads/2017/01/University_of_Rochester_logo.png");
                mTest = new SpeedTest();
            }
        });

    } //onCreate()

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    } //onDestroy

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(KEY_SPEED_TEST_RESULT, mSpeedTestResults.getText());
    } //onSaveInstanceState()

    private class SpeedTest extends AsyncTask<String, Void, Bitmap> {
        private long time;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            time = System.currentTimeMillis();
        } //onPreExecute()

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap result = null;
            try{
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();
                result = BitmapFactory.decodeStream(connection.getInputStream());
            }
            catch (IOException e){
                Log.e(TAG, "IOException thrown");
            }
            return result;
        } //doInBackground()

        @Override
        protected void onPostExecute(Bitmap result){
            super.onPostExecute(result);
            String info = "Speed test completed in " + String.valueOf(System.currentTimeMillis() - time) + " ms";
            mSpeedTestResults.setText(info);
        } //onPostExecute()
    } //end class FetchImage

} //end class CurrentConnectionInfoActivity
