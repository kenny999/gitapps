package com.smallscore.chargewarner;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.numWarnings);
        textView.setText(Logic.getNumberOfEnabledWarningsString(this));

        TextView tView = (TextView) findViewById(R.id.temp);
        tView.setText(Logic.getBatteryTemperatureString(this));

        textView = (TextView) findViewById(R.id.technology);
        textView.setText(Logic.getBatteryTechnology(this));

        textView = (TextView) findViewById(R.id.voltage);
        textView.setText(Logic.getBatteryVoltage(this));
        //doAds();
        new SimpleEula(this).show();
    }

    public void settings(View view){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void statistics(View view){
        startActivity(new Intent(this, StatisticsActivity.class));
    }

    public void recommend(View view){
        startActivity(new Intent(this, RecommendationActivity.class));
    }

    public void help(View view){
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void refresh(View view){
        recreate();
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
