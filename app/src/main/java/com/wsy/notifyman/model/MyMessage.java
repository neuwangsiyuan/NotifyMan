package com.wsy.notifyman.model;

import com.alibaba.fastjson.JSON;

import io.realm.RealmObject;

/**
 * Created by 思远 on 2017/5/7.
 */

public class MyMessage {

    public int code;
    public String data;

    public <T> T toTarget(Class<T> tClass){
        try {
            return JSON.parseObject(data,tClass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
