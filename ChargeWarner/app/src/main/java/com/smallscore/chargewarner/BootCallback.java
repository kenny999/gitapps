package com.smallscore.chargewarner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class BootCallback extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logic.onBoot(context);
    }
}

