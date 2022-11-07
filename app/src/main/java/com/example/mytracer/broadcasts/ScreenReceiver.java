package com.example.mytracer.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mytracer.activities.LockScreen;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("BAM", "Screen went on");
            Intent i = new Intent(context, LockScreen.class);
            context.startActivity(i);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("BAM", "Screen went off");
        }
    }
}
