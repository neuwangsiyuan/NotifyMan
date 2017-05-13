package com.wsy.notifyman.common;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wsy.notifyman.model.MyMessage;
import com.wsy.notifyman.model.master.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 思远 on 2017/5/14.
 * describe ：
 */

public final class Group {

    private Group() {
        //no instance
    }
    private  static Group group = new Group();

    public static Group get(){
        return group;
    }

    private long groupId;
    private List<UserInfo> users = new ArrayList<>();
    private Map<UserInfo,ClientStatus> userStatusMap = new HashMap<>();

    public void init(long groupId, List<UserInfo> list) {
        users.addAll(list);
        this.groupId = groupId;
        Collections.sort(users, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo o1, UserInfo o2) {
                return (int) (o1.getUserID() - o2.getUserID());
            }
        });
        for (UserInfo info:
             users) {
            userStatusMap.put(info,new ClientStatus());
        }
    }

    public boolean isPublishing() {
        return TAG;
    }

    private int curIndex = 0;
    private boolean TAG = false;
    /**
     * 发送故障消息给客户端接受者
     * @param copy
     */
    public void publish(Issue copy) {
        if(TAG)
            return;
        TAG = true;
        final UserInfo targetUser = users.get(curIndex);
        MyMessage m = new MyMessage();
        m.code = 0;
        m.data = JSON.toJSONString(copy);
        Message message = JMessageClient.createSingleTextMessage(targetUser.getUserName(),m.toJson());
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    userStatusMap.get(targetUser).publishTime = System.currentTimeMillis();
                }else{
                    Log.d("DOOZE", "gotResult: "+i+","+s);
                }
            }
        });
        JMessageClient.sendMessage(message);
    }


    public static class ClientStatus{
        public long publishTime;
        public long receivedTime;
        public boolean isHandler;

    }
}
