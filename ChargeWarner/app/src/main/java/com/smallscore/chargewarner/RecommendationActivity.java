package com.smallscore.chargewarner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

public class RecommendationActivity extends Activity {

    private static String TAG = "RecommendationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        //doAds();
    }

    public void sms(View view){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse(Constants.SMS_URI_SCHEME));
        sendIntent.putExtra(Constants.SMS_BODY_EXTRA, getResources().getString(R.string.smsbody));
        startActivity(sendIntent);
    }

    public void rate(View view){
        String uri = Constants.GOOGLE_PLAY_URI + getPackageName();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
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

    public void doAds() {
        Log.d(TAG, "doAds");
        //AdManager.displayBanner(this, (AdView) findViewById(R.id.adView));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
      /*  AdView mAdView = (AdView) findViewById(R.id.adView);
        if(mAdView != null){
            mAdView.destroy();
        }
        */
    }
}
