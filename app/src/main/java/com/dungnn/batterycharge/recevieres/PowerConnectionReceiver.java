package com.dungnn.batterycharge.recevieres;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.dungnn.batterycharge.utilities.BatteryStatus;
import com.dungnn.batterycharge.utilities.ChargeUtil;

/**
 * Created by nndun on 10/11/2017.
 */

public class PowerConnectionReceiver extends BroadcastReceiver {
    BatteryStatus statusConnect;
    BatteryStatus statusDisconnect;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("DUNGNN", intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            statusConnect = new BatteryStatus(context);
            ChargeUtil util = new ChargeUtil(context);
            util.createView();
            Toast.makeText(context, "Bạn đã cắm xạc pin" + statusConnect.getLevel(), Toast.LENGTH_SHORT).show();

            context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    statusDisconnect = new BatteryStatus(context);

                    Toast.makeText(context, "Bạn đã rút xạc pin" + statusConnect.getLevel() + "---" + statusDisconnect.getLevel(), Toast.LENGTH_SHORT).show();



                }
            }, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        }
    }


}
