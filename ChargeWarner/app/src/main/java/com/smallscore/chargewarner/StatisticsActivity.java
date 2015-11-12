package com.smallscore.chargewarner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class StatisticsActivity extends Activity {

    private static String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //doAds();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int total = prefs.getInt(Constants.Preferences.NUM_WARNINGS_PREFERENCE, 0);
        int numScheduledWarnings = prefs.getInt(Constants.Preferences.NUM_SCHEDULED_WARNINGS_PREFERENCE, 0);
        int numBatteryFullWarnings = prefs.getInt(Constants.Preferences.NUM_BATTERY_FULL_WARNINGS_PREFERENCE, 0);
        int numTemperatureWarnings = prefs.getInt(Constants.Preferences.NUM_TEMP_WARNINGS_PREFERENCE, 0);

        TextView totalTextView = (TextView) findViewById(R.id.total);
        TextView numScheduledWarningsTextView = (TextView) findViewById(R.id.numScheduledWarnings);
        TextView numBatteryFullWarningsTextView = (TextView) findViewById(R.id.numBatteryFullWarnings);
        TextView numTemperatureWarningsTextView = (TextView) findViewById(R.id.numTemperatureWarnings);

        totalTextView.setText(""+ total);
        numScheduledWarningsTextView.setText(""+ numScheduledWarnings);
        numBatteryFullWarningsTextView.setText(""+ numBatteryFullWarnings);
        numTemperatureWarningsTextView.setText(""+ numTemperatureWarnings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.help) {
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        } else if(id == R.id.sendsupportmail) {
            startActivity(new Intent(this, SupportEmailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void close(View view){
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        /*
        AdView mAdView = (AdView) findViewById(R.id.adView);
        if(mAdView != null){
            mAdView.destroy();
        }
        */
    }

    public void doAds() {
        Log.d(TAG, "doAds");
        //AdManager.displayBanner(this, (AdView) findViewById(R.id.adView));
    }
}
