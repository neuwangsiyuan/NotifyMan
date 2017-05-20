package com.wsy.notifyman;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wsy.notifyman.server.views.HomeActivity;

/**
 * Created by 思远 on 2017/5/12.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Config.isServer) {
            context.startActivity(new Intent(context, HomeActivity.class));
        }
    }
}
