package com.smallscore.chargewarner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setSummaryOfRingTone();
        new SimpleEula(this).show();
       //String deviceID = DebugUtils.getDeviceID(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        // A patch to overcome OnSharedPreferenceChange not being called by RingtonePreference bug
        RingtonePreference pref = (RingtonePreference) findPreference(Constants.RINGTONE_PREFERENCE);
        pref.setOnPreferenceChangeListener(this);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case Constants.ENABLED_PREFERENCE:
                Logic.onEnabledChanged(this, sharedPreferences);
                break;
            case Constants.WARNING_TIME_PREFERENCE:
                Logic.onWarningTimeChanged(this);
                break;
            case Constants.TEMPERATURE_WARNING_ENABLED_PREFERENCE:
                Logic.onTemperatureWarningChanged(this);
                break;
            case Constants.BATTERY_FULL_WARNING_ENABLED_PREFERENCE:
                Logic.onBatteryFullWarningChanged(this);
                break;
            // case "ringtone": Android has bug for ringtones, using http://stackoverflow.com/questions/6725105/ringtonepreference-not-firing-onsharedpreferencechanged
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference.getKey().equals(Constants.RINGTONE_PREFERENCE)) {
            setSummaryOfRingTone(preference, (String) newValue);
            Logic.onChangedRingTone(this, preference, (String) newValue);
        }
        return true;
    }

    private void showInitialHelp() {
        new InitialHelp(this).show();
    }

    private void setSummaryOfRingTone() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strRingtonePreference = prefs.getString(Constants.RINGTONE_PREFERENCE, Constants.DEFAULT_RINGTONE_URI);
        Uri ringtoneUri = Uri.parse(strRingtonePreference);
        Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        String name = ringtone.getTitle(this);
        Preference p = findPreference(Constants.RINGTONE_PREFERENCE);
        p.setSummary(name);
    }

    private void setSummaryOfRingTone(Preference p, String ringTone) {
        Uri ringtoneUri = Uri.parse(ringTone);
        Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        String name = ringtone.getTitle(this);
        p.setSummary(name);
    }
}
