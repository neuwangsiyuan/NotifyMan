package com.wsy.notifyman.common;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by 思远 on 2017/5/6.
 */

public class IMEIUtils {

    public static String getImei(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
}
