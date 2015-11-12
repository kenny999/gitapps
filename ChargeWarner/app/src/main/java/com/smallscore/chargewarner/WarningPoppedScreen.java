package com.smallscore.chargewarner;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WarningPoppedScreen extends WarningPoppedScreenAbstract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_popped_screen);
        super.doAds();
        updatePreferencesToRunning(Constants.Preferences.WARNING_SCREEN_IS_RUNNING_PREFERENCE, true);
        setFlagsToKeepScreenOn();
        if(savedInstanceState == null){
            WarningService.releaseWakeLock();
        }
        super.play();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        resetSound((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        updatePreferencesToRunning(Constants.Preferences.WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_warning_popped_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
