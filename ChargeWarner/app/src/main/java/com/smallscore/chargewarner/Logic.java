package com.smallscore.chargewarner;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
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
        setAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onEnabledChanged(Context context, SharedPreferences sharedPreferences) {
        setAlarmIfEnabled(context, sharedPreferences);
    }

    public static void onWarningTimeChanged(Context context) {
        setAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onBoot(Context context) {
        setAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void onWarningPopped(Context context) {
        setAlarmIfEnabled(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static boolean shouldPlayWarning(Context context) {
        if(warningCurrentlyPlaying(context)){
            return false;
        }
        if(pluggedToCharger(context)){
            return true;
        }
        return false;
    }

    private static boolean warningCurrentlyPlaying(Context context) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.WARNING_SCREEN_IS_RUNNING_PREFERENCE, false);
    }

    private static void setAlarmIfEnabled(Context context, SharedPreferences sharedPreferences) {
        boolean enabled = sharedPreferences.getBoolean("enabled", false);
        if(enabled){
            WarningAlarmManager.resetWarning(context, makeCalendar(context));
        } else {
            WarningAlarmManager.cancelWarning(context);
        }
    }

    private static Calendar makeCalendar(Context context) {
        boolean stabilityTest = true;
        if(stabilityTest){
            return doStabilityTest();
        }
        Calendar now = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        String warningTime = PreferenceManager.getDefaultSharedPreferences(context).getString("warningTime", null);
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

}
