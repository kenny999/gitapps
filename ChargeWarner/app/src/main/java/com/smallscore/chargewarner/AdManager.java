package com.smallscore.chargewarner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by kenneth on 2015-11-10.
 */
public class AdManager {

    private static String TAG = "AdManager";
    private static boolean testing = false;

    private static String getInterstitialAdUnitId(Context context) {
        if(testing){
            return Constants.Ads.INTERSTITIAL_TEST_AD;
        } else {
            return context.getString(R.string.AdAfterWarningMiddlePage);
        }
    }

    public static AdRequest.Builder getAdBuilder() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if(testing){
            builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice(Constants.Ads.KENNETH_DEVICE_ID)
                    .addTestDevice(Constants.Ads.MARTINA_DEVICE_ID)
                    .addTestDevice(Constants.Ads.CHRISTINA_DEVICE_ID);
        }
        return builder;
    }

    private static boolean shouldDisplayBanner(Activity activity) {
        return false;
        /*
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        int currentNumber = sharedPreferences.getInt(Constants.Preferences.NUM_WARNINGS_PREFERENCE, 0);
        if(activity instanceof WarningPoppedScreenAbstract) {
            return currentNumber > Constants.Ads.NUM_ADS_BEFORE_SHOWING_BANNER_WARNING_PAGE;
        } else if(activity instanceof RecommendationActivity){
            return currentNumber > Constants.Ads.NUM_ADS_BEFORE_SHOWING_BANNER_RECOMMENDATION_PAGE;
        } else if(activity instanceof StatisticsActivity){
            return currentNumber > Constants.Ads.NUM_ADS_BEFORE_SHOWING_BANNER_STATISTICS_PAGE;
        }else if(activity instanceof MainActivity){
            return currentNumber > Constants.Ads.NUM_ADS_BEFORE_SHOWING_BANNER_MAIN_PAGE;
        }
        return false;
        */
    }

    private static boolean shouldDisplayInterstitialAd(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentNumber = sharedPreferences.getInt(Constants.Preferences.NUM_WARNINGS_PREFERENCE, 0);
        return currentNumber > Constants.Ads.NUM_ADS_BEFORE_SHOWING_INSTERSTITIAL_AD;
    }

    public static void displayBanner(Activity activity, AdView adView) {
        if(shouldDisplayBanner(activity)) {
            Log.d(TAG, "Displaying banner");
            AdRequest request;
            AdRequest.Builder builder = AdManager.getAdBuilder();
            request = builder.build();
            adView.loadAd(request);
        }
    }

    public static InterstitialAd loadInterstitialAd(Context context) {
        if(shouldDisplayInterstitialAd(context)) {
            Log.d(TAG, "Displaying Interstitial");
            InterstitialAd interstitialAd = new InterstitialAd(context.getApplicationContext());
            interstitialAd.setAdUnitId(getInterstitialAdUnitId(context));
            AdRequest request;
            AdRequest.Builder builder = getAdBuilder();
            request = builder.build();
            interstitialAd.loadAd(request);
            return interstitialAd;
        } else {
            return null;
        }
    }
}
