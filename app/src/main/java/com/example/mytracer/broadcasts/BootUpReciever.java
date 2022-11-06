package com.example.mytracer.broadcasts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mytracer.activities.MainActivity;

public class BootUpReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("in broad....");
        Log.i("BroadcastReceiver", "in broad....");


        if ((intent.getAction() != null) &&
                (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")))
        {

            System.out.println("in broadcast receiver.....");
            Log.i("BroadcastReceiver", "onReceive: Broadcast Received");
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

}
