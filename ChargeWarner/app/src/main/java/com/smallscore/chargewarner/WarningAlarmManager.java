package com.smallscore.chargewarner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by kenneth on 2015-08-13.
 */
public class WarningAlarmManager {

    public static void cancelWarning(Context context){
        PendingIntent pIntent = createPendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    public static void resetWarning(Context context, Calendar newWarningTime) {
        cancelWarning(context);
        PendingIntent pIntent = createPendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        scheduleWarning(newWarningTime, pIntent, alarmManager);
    }

    public static void resetTemperatureWarning(Context context, Calendar newTemperatureWarningTime) {
        cancelTemperatureWarning(context);
        PendingIntent pIntent = createTemperaturePendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        scheduleWarning(newTemperatureWarningTime, pIntent, alarmManager);
    }

    public static void cancelTemperatureWarning(Context context) {
        PendingIntent pIntent = createTemperaturePendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    public static void resetBatteryFullWarning(Context context, Calendar calendar) {
        cancelBatteryFullWarning(context);
        PendingIntent pIntent = createBatteryFullPendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        scheduleWarning(calendar, pIntent, alarmManager);
    }

    public static void cancelBatteryFullWarning(Context context) {
        PendingIntent pIntent = createBatteryFullPendingIntent(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    private static PendingIntent createBatteryFullPendingIntent(Context context) {
        Intent myIntent = new Intent(context, BatteryFullBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static void scheduleWarning(Calendar calendar, PendingIntent pIntent, AlarmManager alarmManager) {
        Log.d("WarningAlarmManager", "Scheduling for " + calendar);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent myIntent = new Intent(context, WarningBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent createTemperaturePendingIntent(Context context) {
        Intent myIntent = new Intent(context, TemperatureWarningBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
