package com.smallscore.chargewarner;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Created by kenneth on 2015-08-13.
 */
public class WarningService extends Service{

    public static final String TAG = "WarningService";
    private static PowerManager.WakeLock mWakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        acquireWakeLock(pm);
        if(intent != null) {
            Intent alarmIntent = new Intent(getBaseContext(), WarningPoppedScreen.class);
            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            alarmIntent.putExtras(intent);
            getApplication().startActivity(alarmIntent);
            Logic.onWarningPopped(this);
        }
        return START_NOT_STICKY;
    }

    private static synchronized void acquireWakeLock(PowerManager pm) {
        mWakeLock = null;
        // Acquire wakelock
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }

    public static synchronized void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mWakeLock = null;
    }
}
