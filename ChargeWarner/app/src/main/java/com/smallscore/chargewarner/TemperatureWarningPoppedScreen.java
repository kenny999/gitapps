package com.smallscore.chargewarner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;


public class TemperatureWarningPoppedScreen extends WarningPoppedScreenAbstract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempwarning_popped_screen);
        super.doAds();
        updatePreferencesToRunning(Constants.TEMPERATURE_WARNING_SCREEN_IS_RUNNING_PREFERENCE, true);
        setFlagsToKeepScreenOn();
        if(savedInstanceState == null){
            TemperatureWarningService.releaseWakeLock();
        }
        super.play();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        resetSound((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        updatePreferencesToRunning(Constants.TEMPERATURE_WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
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
