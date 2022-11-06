package com.example.mytracer.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mytracer.activities.MainActivity;

public class StartupOnBootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Intent activityIntent = new Intent(context, MainActivity.class);

            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(activityIntent);
        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // TODO Auto-generated method stub
//        Intent myIntent = new Intent(context, MainActivity.class);
//        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        context.startActivity(myIntent);
//    }
}
