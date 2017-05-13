package com.wsy.notifyman.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.wsy.notifyman.common.IssuePublish;
import com.wsy.notifyman.model.master.Issue;
import com.wsy.notifyman.model.master.IssueFactory;

/**
 * Created by 思远 on 2017/5/12.
 *
 * 模拟机房发生故障，负责生产故障消息
 */

public class CreatorServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    monitoring();
                }
            }
        }).start();
    }

    /**
     * 此处实现故障生成
     */
    private void monitoring() {
        double r = Math.random() * IssueFactory.TAG;
        IssueFactory.CHAOS();
        int i = IssueFactory.Pick((int) r);
        if(i < IssueFactory.COUNT) {
            Issue issue = IssueFactory.build(i); // 产生故障
            IssuePublish.get().publish(issue); //将故障信息发送给上层处理
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
