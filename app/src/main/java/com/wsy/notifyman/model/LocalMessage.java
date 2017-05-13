package com.wsy.notifyman.model;

import io.realm.RealmObject;

/**
 * Created by 思远 on 2017/5/7.
 */

public class LocalMessage extends RealmObject {

    public String data;
    public long createTime;

    @Override
    public String toString() {
        return "LocalMessage{" +
                "data='" + data + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
