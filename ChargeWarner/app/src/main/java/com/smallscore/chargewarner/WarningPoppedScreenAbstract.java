package com.smallscore.chargewarner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

/**
 * Created by kenneth on 2015-10-13.
 */
public abstract class WarningPoppedScreenAbstract extends Activity {

    private static final String TAG = "WPSAbstract";
    private InterstitialAd mInterstitialAd;

    private MediaPlayer mediaPlayer;
    private int oldVolume;
    private boolean deviceSilent;

    protected void setupMedia(AudioManager am) {
        mediaPlayer = new MediaPlayer();
        oldVolume = am.getStreamVolume(AudioManager.STREAM_ALARM);
    }

    protected void resetSound(AudioManager am) {
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

    protected static boolean deviceSilentAndNotAllowedToPlay(Context context) {
        final AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        if(ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            boolean playWhenSilent = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Constants.PLAY_WHEN_SILENT_PREFERENCE, false);
            if(! playWhenSilent){
                return true;
            }
        }
        return false;
    }

    protected void playAlarmTone() {
        try {
            String uriStr = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.RINGTONE_PREFERENCE, null);
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

    protected void setVolume() {
        double volume = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.WARNING_VOLUME_PREFERENCE, 0);
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

    protected void setFlagsToKeepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    protected void updatePreferencesToRunning(String preferenceName, boolean running) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(preferenceName, running);
        editor.commit();
    }

    protected void createDismissButton(final AudioManager am) {
        Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                resetSound(am);
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                finish();
            }
        });
    }

    public void doAds() {
        Log.d(TAG, "doAds");
        boolean testing = false;
        String interstitialAdUnit = null;
        if(testing){
            interstitialAdUnit = Constants.INTERSTITIAL_TEST_AD;
        } else {
            interstitialAdUnit = getString(R.string.AdAfterWarningMiddlePage);
        }
        if(Logic.shouldDisplayBanner(this)) {
            Log.d(TAG, "Displaying banner");
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest request;
            AdRequest.Builder builder = new AdRequest.Builder();
            if(testing){
                builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                        .addTestDevice(Constants.KENNETH_DEVICE_ID)
                        .addTestDevice(Constants.MARTINA_DEVICE_ID)
                        .addTestDevice(Constants.CHRISTINA_DEVICE_ID);
            }
            request = builder.build();
            mAdView.loadAd(request);
        }
        if(Logic.shouldDisplayInterstitialAd(this)) {
            Log.d(TAG, "Displaying Interstitial");
            mInterstitialAd = new InterstitialAd(getApplicationContext());
            mInterstitialAd.setAdUnitId(interstitialAdUnit);
            AdRequest request;
            AdRequest.Builder builder = new AdRequest.Builder();
            if(testing){
                builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                        .addTestDevice(Constants.KENNETH_DEVICE_ID)
                        .addTestDevice(Constants.MARTINA_DEVICE_ID)
                        .addTestDevice(Constants.CHRISTINA_DEVICE_ID);
            }
            request = builder.build();
            mInterstitialAd.loadAd(request);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    finish();
                }
            });
        }
    }

    public void play(){
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
        AdView mAdView = (AdView) findViewById(R.id.adView);
        if(mAdView != null){
            mAdView.destroy();
        }
    }
}
