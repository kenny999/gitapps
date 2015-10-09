package com.smallscore.chargewarner;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by kenneth on 2015-08-13.
 */
public class WarningService extends IntentService {

    public static final String TAG = "WarningService";
    private static PowerManager.WakeLock mWakeLock;

    public WarningService() {
        super("WarningService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "In "+TAG);
        try {
            if(! Logic.shouldPlayWarning(getBaseContext())){
                return;
            }
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            acquireWakeLock(pm);
            Intent alarmIntent = new Intent(getBaseContext(), WarningPoppedScreen.class);
            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(alarmIntent);
        } finally {
            Logic.onWarningPopped(this);
            if(intent != null) {
                WarningBroadcastReceiver.completeWakefulIntent(intent);
            }
        }

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
