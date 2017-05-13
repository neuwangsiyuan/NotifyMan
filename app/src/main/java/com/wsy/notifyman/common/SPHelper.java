package com.wsy.notifyman.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 思远 on 2017/5/6.
 */

public final class SPHelper {
    private SPHelper() {
        //no instance
    }

    private static SPHelper sp;
    private SharedPreferences sharedPreferences;

    public static SPHelper get(){
        if(sp == null){
            sp = new SPHelper();
        }
        return sp;
    }

    public void init(Context appContext,String spName){
        sharedPreferences = appContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
    }

    public void putString(String key,String value){
        sharedPreferences.edit().putString(key,value).commit();
    }
    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }

    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key,value).commit();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key,0);
    }
}
