package com.dungnn.batterycharge.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dungnn.batterycharge.recevieres.PowerConnectionReceiver;

/**
 * Created by nndun on 10/11/2017.
 */

public class ChargeService extends Service {
    private PowerConnectionReceiver powerReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        registerChargeReceiver();
    }

    @Override
    public void onDestroy() {
        unregisterChargeReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void registerChargeReceiver() {
        powerReceiver = new PowerConnectionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(powerReceiver,filter);
    }
    private void unregisterChargeReceiver() {
        try {
            if (powerReceiver != null) {
                unregisterReceiver(powerReceiver);
            }
        } catch (IllegalArgumentException e) {}
    }
}

