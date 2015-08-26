package com.smallscore.chargewarner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

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

    private static void scheduleWarning(Calendar calendar, PendingIntent pIntent, AlarmManager alarmManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, WarningService.class);
        return PendingIntent.getService(context, (int) 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
