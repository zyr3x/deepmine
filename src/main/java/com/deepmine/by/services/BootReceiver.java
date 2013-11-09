package com.deepmine.by.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zyr3x on 04.10.13.
 */
public class BootReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                launchService(context);
            }
        }

        public static void launchService(Context context) {

            Intent intent = new Intent();
            intent.setClass(context, EventService.class);
            context.startService(intent);
        }
}
