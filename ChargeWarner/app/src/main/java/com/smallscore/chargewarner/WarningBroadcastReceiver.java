package com.smallscore.chargewarner;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by kenneth on 2015-08-27.
 */
public class WarningBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, WarningService.class);
        startWakefulService(context, serviceIntent);
    }
}