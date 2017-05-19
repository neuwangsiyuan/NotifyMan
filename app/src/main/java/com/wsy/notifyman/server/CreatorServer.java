package com.wsy.notifyman.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.ALog;
import com.wsy.notifyman.Config;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.model.master.Issue;
import com.wsy.notifyman.model.master.IssueFactory;

/**
 * Created by 思远 on 2017/5/12.
 * <p>
 * 模拟机房发生故障，负责生产故障消息
 */

public class CreatorServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long curTime;


    @Override
    public void onCreate() {
        super.onCreate();
        PC.get().init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Group.get().isInit()) {
                        testing();
                        monitoring();
                    }
                }
            }
        }).start();
    }

    private Issue curIssue;

    private void testing() {
        if (Group.get().isReceived()) {
            curIssue = Group.get().nextIssue();
            if (curIssue != null) {
                curTime = System.currentTimeMillis();
                Group.get().publish(curIssue);
            }
        } else {
            if (System.currentTimeMillis() - curTime > Config.TIME_GAP) {
                //所有用户30s内都没有接受到信息,直接发送下一个消息
                if (Group.get().nextValidPublic()) {
                    curIssue = Group.get().nextIssue();
                    if (curIssue != null) {
                        Group.get().publish(curIssue);
                    }
                } else {
                    Group.get().publish(curIssue);
                }
                curTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 此处实现故障生成
     */
    private long timeGap;

    private void monitoring() {
        if (System.currentTimeMillis() - timeGap > 10000) {

            if (PC.get().isAlert()) {
                Issue issue = IssueFactory.build(PC.get()); // 产生故障
                ALog.d(issue);
                if (issue != null)
                    Group.get().newIssue(issue);
            }
            timeGap = System.currentTimeMillis();
            PC.get().init();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
