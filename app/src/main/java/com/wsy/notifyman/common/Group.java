package com.wsy.notifyman.common;

import com.alibaba.fastjson.JSON;
import com.blankj.ALog;
import com.wsy.notifyman.Config;
import com.wsy.notifyman.model.MyMessage;
import com.wsy.notifyman.model.master.Issue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
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

    private static Group group = new Group();

    public static Group get() {
        return group;
    }

    private long groupId;
    private LinkedList<Issue> issues;
    private List<UserInfo> users = new ArrayList<>();
    private Map<UserInfo, ClientStatus> userStatusMap = new HashMap<>();

    public List<Issue> getIssues() {
        return issues;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public void init(long groupId, List<UserInfo> list) {
        users.addAll(list);
        this.groupId = groupId;
        Collections.sort(users, new Comparator<UserInfo>() {
            @Override
            public int compare(UserInfo o1, UserInfo o2) {
                return (int) (o1.getUserID() - o2.getUserID());
            }
        });
        for (UserInfo info :
                users) {
            userStatusMap.put(info, new ClientStatus());
        }
        issues = new LinkedList<>();
        isInit = true;
    }

    public long myGroupId() {
        return groupId;
    }

    private int curIndex = 0;
    private boolean isInit = false;

    /**
     * 发送故障消息给客户端接受者
     *
     * @param copy
     */
    public void publish(Issue copy) {
        final UserInfo targetUser = users.get(curIndex);
        MyMessage m = new MyMessage();
        m.code = Config.CODE_SERVER_ISSUE;
        m.data = JSON.toJSONString(copy);
        Message message = JMessageClient.createSingleTextMessage(targetUser.getUserName(), m.toJson());
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    userStatusMap.get(targetUser).publishTime = System.currentTimeMillis();
                } else {
                    ALog.d("gotResult: " + i + "," + s);
                }
            }
        });
        JMessageClient.sendMessage(message);
    }

    //收到客户端的回执信息
    public void respond(UserInfo fromUser) {
        ClientStatus status = userStatusMap.get(fromUser);
        status.isHandler = true;
        status.receivedTime = System.currentTimeMillis();
        curIndex = 0;
    }

    public boolean isReceived() {
        ClientStatus status = userStatusMap.get(users.get(curIndex));
        return status.isHandler;
    }

    public void reset() {
        for (int i = 0; i < users.size(); i++) {
            userStatusMap.get(users.get(i)).isHandler = false;
        }
    }

    public boolean isInit() {
        return isInit;
    }

    public boolean nextValidPublic() {
        curIndex++;
        if (curIndex >= users.size()) {
            curIndex = 0;
            return true;
        }
        return false;
    }

    public Issue nextIssue() {
        if (issues.isEmpty())
            return null;
        reset();
        return issues.poll();
    }


    /**
     * 故障过滤
     *
     * @param issue
     */
    private boolean filter(Issue issue) {
        int index = issues.indexOf(issue); //队列中不包含同类型的消息则返回-1；
        if (index == -1) {
            return true;
        }
        Issue thisIssue = issues.get(index);
        thisIssue.countAdd();
        thisIssue.updatePCInfo(issue);
        return false;
    }

    /**
     * 产生的故障先经过过滤，过滤方法返回true说明，当前队列不含同类型的故障消息
     *
     * @param issue
     */
    public void newIssue(Issue issue) {
        if (filter(issue)) {
            issues.add(issue);
        }
    }

    /**
     * 客户端收到故障消息后，自动发送回执消息
     *
     * @param fromUser
     */
    public void clientRespond(UserInfo fromUser) {
        MyMessage m = new MyMessage();
        m.code = Config.CODE_CLIENT_RESPOND;
        m.data = "";
        Message message = JMessageClient.createSingleTextMessage(fromUser.getUserName(), m.toJson());
        message.setOnSendCompleteCallback(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                } else {
                    ALog.d("DOOZE", "gotResult: " + i + "," + s);
                }
            }
        });
        JMessageClient.sendMessage(message);
    }


    public static class ClientStatus {
        public long publishTime;
        public long receivedTime;
        public boolean isHandler;

    }
}
