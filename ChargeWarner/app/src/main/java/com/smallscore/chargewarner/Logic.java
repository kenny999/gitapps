package com.smallscore.chargewarner;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.preference.Preference;
import android.preference.PreferenceManager;

import java.util.Calendar;

/**
 * Created by kenneth on 2015-08-19.
 */
public class Logic {

    public static void onChangedRingTone(Context context, Preference preference, String uriStr) {
        Uri uri = Uri.parse(uriStr);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        if (ringtone != null) {
            preference.setSummary(ringtone.getTitle(context));
        }
        setAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onEnabledChanged(Context context, SharedPreferences sharedPreferences) {
        setAlarmIfEnabledAndChargerPlugged(context, sharedPreferences);
    }

    public static void onWarningTimeChanged(Context context) {
        setAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onBoot(Context context) {
        resetCurrentPlayingPreferences(context);
        setAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
        setBatteryFullAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
        setTemperatureAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onWarningPopped(Context context) {
        increaseNumberOfWarnings(context);
        setAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static boolean shouldPlayWarning(Context context) {
        if(anyWarningCurrentlyPlaying(context)){
            return false;
        }
        if(pluggedToCharger(context)){
            return true;
        }
        return false;
    }

    public static boolean anyWarningCurrentlyPlaying(Context context) {
        return warningCurrentlyPlaying(context) || temperatureWarningCurrentlyPlaying(context) || batteryFullWarningCurrentlyPlaying(context);
    }

    public static boolean shouldPlayTemperatureWarning(Context context) {
        if(anyWarningCurrentlyPlaying(context)){
            return false;
        }
        if(! pluggedToCharger(context)){
            return false;
        }
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, ifilter);
        final int health  = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        final int temperatureInDegrees = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
        boolean overheat = health == BatteryManager.BATTERY_HEALTH_OVERHEAT;
        int  temperaturePref = PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.TEMPERATURE_PREFERENCE, Constants.HIGH_TEMPERATURE);
        boolean highTemperature = temperatureInDegrees >= temperaturePref;
        return (overheat || highTemperature);
    }

    public static void onTemperatureWarningPopped(Context context) {
        increaseNumberOfWarnings(context);
        setTemperatureAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onBatteryFullWarningPopped(Context context) {
        increaseNumberOfWarnings(context);
        setBatteryFullAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    private static void increaseNumberOfWarnings(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentNumber = sharedPreferences.getInt(Constants.NUM_WARNINGS_PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.NUM_WARNINGS_PREFERENCE, currentNumber + 1);
        editor.commit();
    }

    public static void onTemperatureWarningChanged(Context context) {
        setTemperatureAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onAppStarted(Context context) {
        resetCurrentPlayingPreferences(context);
        setAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
        setBatteryFullAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
        setTemperatureAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static boolean shouldDisplayBanner(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentNumber = sharedPreferences.getInt(Constants.NUM_WARNINGS_PREFERENCE, 0);
        return currentNumber > Constants.NUM_ADS_BEFORE_SHOWING_BANNER;
    }

    public static boolean shouldDisplayInterstitialAd(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int currentNumber = sharedPreferences.getInt(Constants.NUM_WARNINGS_PREFERENCE, 0);
        return currentNumber > Constants.NUM_ADS_BEFORE_SHOWING_INSTERSTITIAL_AD;
    }

    public static void onChargerPlugged(Context context) {
        setAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
        setBatteryFullAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
        setTemperatureAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onChargerUnplugged(Context context) {
        WarningAlarmManager.cancelWarning(context);
        WarningAlarmManager.cancelBatteryFullWarning(context);
        WarningAlarmManager.cancelTemperatureWarning(context);
    }

    public static boolean shouldPlayBatteryFullWarning(Context context) {
        if(anyWarningCurrentlyPlaying(context)){
            return false;
        }
        if(! pluggedToCharger(context)){
            return false;
        }
        return batteryFullyCharged(context);
    }

    public static void onBatteryFullWarningChanged(Context context) {
        setBatteryFullAlarmIfEnabledAndChargerPlugged(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    private static void resetCurrentPlayingPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
        editor.putBoolean(Constants.TEMPERATURE_WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
        editor.putBoolean(Constants.BATTERY_FULL_WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
        editor.commit();
    }

    private static boolean warningCurrentlyPlaying(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
    }

    private static boolean temperatureWarningCurrentlyPlaying(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.TEMPERATURE_WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
    }

    private static boolean batteryFullWarningCurrentlyPlaying(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.BATTERY_FULL_WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
    }

    private static void setAlarmIfEnabledAndChargerPlugged(Context context, SharedPreferences sharedPreferences) {
        boolean enabled = sharedPreferences.getBoolean(Constants.ENABLED_PREFERENCE, true);
        boolean chargerPlugged = pluggedToCharger(context);
        if(enabled && chargerPlugged){
            WarningAlarmManager.resetWarning(context, makeCalendar(context));
        } else {
            WarningAlarmManager.cancelWarning(context);
        }
    }

    private static void setBatteryFullAlarmIfEnabledAndChargerPlugged(Context context, SharedPreferences sharedPreferences) {
        boolean enabled = sharedPreferences.getBoolean(Constants.BATTERY_FULL_WARNING_ENABLED_PREFERENCE, true);
        boolean chargerPlugged = pluggedToCharger(context);
        if(enabled && chargerPlugged){
            WarningAlarmManager.resetBatteryFullWarning(context, makeBatteryFullCalendar());
        } else {
            WarningAlarmManager.cancelBatteryFullWarning(context);
        }
    }

    public static void setTemperatureAlarmIfEnabledAndChargerPlugged(Context context, SharedPreferences sharedPreferences) {
        boolean temperatureWarningEnabled = sharedPreferences.getBoolean(Constants.TEMPERATURE_WARNING_ENABLED_PREFERENCE, true);
        boolean chargerPlugged = pluggedToCharger(context);
        if(temperatureWarningEnabled && chargerPlugged){
            WarningAlarmManager.resetTemperatureWarning(context, makeTemperatureCalendar());
        } else {
            WarningAlarmManager.cancelTemperatureWarning(context);
        }
    }

    private static void setAlarmIfEnabled(Context context, SharedPreferences sharedPreferences) {
        boolean enabled = sharedPreferences.getBoolean(Constants.ENABLED_PREFERENCE, true);
        if(enabled){
            WarningAlarmManager.resetWarning(context, makeCalendar(context));
        } else {
            WarningAlarmManager.cancelWarning(context);
        }
    }

    private static void setBatteryFullAlarmIfEnabled(Context context, SharedPreferences sharedPreferences) {
        boolean enabled = sharedPreferences.getBoolean(Constants.BATTERY_FULL_WARNING_ENABLED_PREFERENCE, true);
        if(enabled){
            WarningAlarmManager.resetBatteryFullWarning(context, makeBatteryFullCalendar());
        } else {
            WarningAlarmManager.cancelBatteryFullWarning(context);
        }
    }

    public static void setTemperatureAlarmIfEnabled(Context context, SharedPreferences sharedPreferences) {
        boolean temperatureWarningEnabled = sharedPreferences.getBoolean(Constants.TEMPERATURE_WARNING_ENABLED_PREFERENCE, true);
        if(temperatureWarningEnabled){
            WarningAlarmManager.resetTemperatureWarning(context, makeTemperatureCalendar());
        } else {
            WarningAlarmManager.cancelTemperatureWarning(context);
        }
    }

    private static Calendar makeCalendar(Context context) {
        boolean stabilityTest = false;
        if(stabilityTest){
            return doStabilityTest();
        }
        Calendar now = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        String warningTime = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.WARNING_TIME_PREFERENCE, null);
        if(warningTime == null){
            warningTime = TimePreference.getDefaultTime();
        }
        int warningHour = TimePreference.getHour(warningTime);
        int warningMinute = TimePreference.getMinute(warningTime);
        calendar.set(Calendar.HOUR_OF_DAY, warningHour);
        calendar.set(Calendar.MINUTE, warningMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String s1 = now.get(Calendar.MONTH)+":"+now.get(Calendar.DAY_OF_MONTH)+":"+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE);
        String s2 = calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

        if(now.before(calendar)){
            // schedule for today.
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar;
    }

    private static Calendar doStabilityTest() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);
        return now;
    }

    private static boolean pluggedToCharger(Context context) {
        // From http://developer.android.com/training/monitoring-device-state/battery-monitoring.html
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        return isCharging;
    }

    private static boolean batteryFullyCharged(Context context) {
        // From http://developer.android.com/training/monitoring-device-state/battery-monitoring.html
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
        return isFull;
    }

    private static Calendar makeTemperatureCalendar() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, Constants.CHECK_TEMPERATURE_INTERVAL_MINUTES);
        return now;
    }

    private static Calendar makeBatteryFullCalendar() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, Constants.CHECK_BATTERY_FULL_INTERVAL_MINUTES);
        return now;
    }
}
