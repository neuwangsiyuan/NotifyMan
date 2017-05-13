package com.wsy.notifyman.model;

import io.realm.RealmObject;

/**
 * Created by 思远 on 2017/5/7.
 */

public class Apply extends RealmObject {
    public String applyReason;
    public String applyUser;
    public int status;
    public long applyTime;

    @Override
    public String toString() {
        return "Apply{" +
                "applyReason='" + applyReason + '\'' +
                ", applyUser='" + applyUser + '\'' +
                ", status=" + status +
                ", applyTime=" + applyTime +
                '}';
    }
}
