package com.googry.coinonehelper.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by seokjunjeong on 2017. 6. 1..
 */

public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("googry", "RestartReceiver called : " + intent.getAction());

        /**
         * 서비스 죽일때 알람으로 다시 서비스 등록
         */
        if (intent.getAction().equals("ACTION.RESTART.PersistentService")) {

            Log.i("googry", "ACTION.RESTART.PersistentService ");

            Intent i = new Intent(context, PersistentService.class);
            context.startService(i);
        }

        /**
         * 폰 재시작 할때 서비스 등록
         */
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            Log.i("googry", "ACTION_BOOT_COMPLETED");
            Intent i = new Intent(context, PersistentService.class);
            context.startService(i);

        }


    }
}
