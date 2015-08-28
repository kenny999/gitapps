package com.smallscore.chargewarner;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
    private int oldRingerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_popped_screen);
        setFlagsToKeepScreenOn();
        WarningService.releaseWakeLock();
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        setupMedia(am);
        createDismissButton(am);
        playAlarmTone();
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
        oldRingerMode = am.getRingerMode();
    }

    private void resetSound(AudioManager am) {
        try { mediaPlayer.stop(); } catch(Exception e){}
        try { mediaPlayer.release(); } catch(Exception e){}
        am.setStreamVolume(AudioManager.STREAM_ALARM, oldVolume, 0);
        am.setRingerMode(oldRingerMode);
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
            boolean playOnMaxVolume = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("playOnMaxVolume", false);
            AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            if(playOnMaxVolume){
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
            }
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
