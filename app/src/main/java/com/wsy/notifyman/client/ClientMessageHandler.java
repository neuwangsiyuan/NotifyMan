package com.wsy.notifyman.client;

import com.alibaba.fastjson.JSON;
import com.blankj.ALog;
import com.hss01248.notifyutil.NotifyUtil;
import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.client.views.MessageDetailActivity;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.common.MessageHandler;
import com.wsy.notifyman.event.ClientRefreshEvent;
import com.wsy.notifyman.model.LocalIssue;
import com.wsy.notifyman.model.MyMessage;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import io.realm.Realm;

/**
 * Created by 思远 on 2017/5/7.
 */

public class ClientMessageHandler implements MessageHandler {
    @Override
    public void handler(final Message message) {
        if (message.getFromUser().equals(JMessageClient.getMyInfo()))
            return;
        switch (message.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) message.getContent();
                String content = textContent.getText();
                final MyMessage myMessage = JSON.parseObject(content, MyMessage.class);
                ALog.d(myMessage);
                switch (myMessage.code) {
                    case Config.CODE_SERVER_ISSUE:
                        Group.get().clientRespond(message.getFromUser());
                        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                LocalIssue issue = new LocalIssue();
                                issue.id = String.valueOf(System.currentTimeMillis());
                                issue.createdAt = message.getCreateTime();
                                issue.origin = myMessage.data;
                                realm.copyToRealmOrUpdate(issue);
                                ALog.d(issue);
                            }
                        });

                        NotifyUtil.buildSimple(message.getId(), R.mipmap.ic_launcher, message.getFromUser().getUserName(),
                                myMessage.data, NotifyUtil.buildIntent(MessageDetailActivity.class)).show();
                        break;
                }
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) message.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        EventBus.getDefault().post(new ClientRefreshEvent());
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                }
                break;

        }
    }
}
