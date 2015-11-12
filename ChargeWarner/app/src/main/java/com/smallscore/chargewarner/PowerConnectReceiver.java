package com.smallscore.chargewarner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerConnectReceiver extends BroadcastReceiver {
    public PowerConnectReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Logic.onChargerPlugged(context);
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Logic.onChargerUnplugged(context);
        }
    }
}
