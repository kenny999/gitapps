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


public class WarningPoppedScreen extends Activity {

    private MediaPlayer mediaPlayer;
    private int oldVolume;
    private boolean deviceSilent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_popped_screen);
        updatePreferencesToRunning(true);
        setFlagsToKeepScreenOn();
        if(savedInstanceState == null){
            WarningService.releaseWakeLock();
        }
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        createDismissButton(am);
        deviceSilent = deviceSilentAndNotAllowedToPlay(this);
        if(! deviceSilent) {
            setupMedia(am);
            playAlarmTone();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        resetSound((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        updatePreferencesToRunning(false);
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

    private void updatePreferencesToRunning(boolean running) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.WARNING_SCREEN_IS_RUNNING_PREFERENCE, running);
        editor.commit();
    }

    private void createDismissButton(final AudioManager am) {
        Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                resetSound(am);
                finish();
            }
        });
    }

    private void setupMedia(AudioManager am) {
        mediaPlayer = new MediaPlayer();
        oldVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
    }

    private void resetSound(AudioManager am) {
        if(! deviceSilent) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
            }
            try {
                mediaPlayer.release();
            } catch (Exception e) {
            }
            am.setStreamVolume(AudioManager.STREAM_ALARM, oldVolume, 0);
        }
    }

    private static boolean deviceSilentAndNotAllowedToPlay(Context context) {
        final AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        if(ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            boolean playWhenSilent = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("playWhenSilent", false);
            if(! playWhenSilent){
                return true;
            }
        }
        return false;
    }

    private void setFlagsToKeepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    private void playAlarmTone() {
        try {
            String uriStr = PreferenceManager.getDefaultSharedPreferences(this).getString("ringtone", null);
            Uri uri = Uri.parse(uriStr);
            setVolume();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            mediaPlayer.release();
            try {
                playFallbackSound();
            } catch(Exception e2){
                e2.printStackTrace();
            }
        }
    }

    private void setVolume() {
        double volume = PreferenceManager.getDefaultSharedPreferences(this).getInt("warningVolume", 0);
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        double streamMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        double streamVolume = (streamMaxVolume / Constants.WARNING_MAX_VOLUME) * volume;
        int roundedStreamVolume = (int) Math.round(streamVolume);
        am.setStreamVolume(AudioManager.STREAM_ALARM, roundedStreamVolume, 0);
    }

    private void playFallbackSound() throws IOException {
        AssetFileDescriptor afd = null;
        try {
            afd = getResources().openRawResourceFd(R.raw.beep);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } finally {
            if(afd != null){
                afd.close();
            }
        }
    }
}
