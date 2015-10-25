package com.smallscore.chargewarner;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BatteryFullBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, BatteryFullService.class);
        startWakefulService(context, serviceIntent);
    }
}
