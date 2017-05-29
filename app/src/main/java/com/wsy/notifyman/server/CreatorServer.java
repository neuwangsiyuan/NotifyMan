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
        //生产线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //群组初始化完成后才开始，模拟发生故障
                    if (Group.get().isInit()) {
                        testing();
                        monitoring();
                    }
                }
            }
        }).start();
    }

    private Issue curIssue;
    private boolean isFirst = true;

    /**
     * 检测并发送故障
     */
    private void testing() {
        //当前的用户接收并发送了回执消息
        if (Group.get().isReceived()) {
            //获取下一条消息
            curIssue = Group.get().nextIssue();
            if (curIssue != null) {
                curTime = System.currentTimeMillis();
                Group.get().publish(curIssue);
            }
        } else {
            //否则，如果是第一次开始发送消息
            if (isFirst) {
                isFirst = false;
                curIssue = Group.get().nextIssue();
                if (curIssue != null)
                    Group.get().publish(curIssue);
                //等待超时后发送给下一个用户
            } else if (System.currentTimeMillis() - curTime > Config.TIME_GAP) {

                //所有用户30s内都没有接受到信息,直接发送下一个消息
                if (Group.get().nextValidPublic()) {
                    curIssue = Group.get().nextIssue();
                }
                if (curIssue != null) {
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
        //10s则初始化一次服务器信息
        if (System.currentTimeMillis() - timeGap > 10000) {
            //如果当前服务器产生故障
            if (PC.get().isAlert()) {
                Issue issue = IssueFactory.build(PC.get()); // 产生故障
                ALog.d(issue);
                Group.get().newIssue(issue);
            }
            timeGap = System.currentTimeMillis();
            PC.get().init(); //重新初始化服务器信息
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
