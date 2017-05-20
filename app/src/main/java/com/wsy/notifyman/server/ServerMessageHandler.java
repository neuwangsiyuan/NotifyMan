package com.wsy.notifyman.server;

import com.alibaba.fastjson.JSON;
import com.blankj.ALog;
import com.wsy.notifyman.Config;
import com.wsy.notifyman.common.Group;
import com.wsy.notifyman.common.MessageHandler;
import com.wsy.notifyman.model.MyMessage;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by 思远 on 2017/5/7.
 */

public class ServerMessageHandler implements MessageHandler {
    @Override
    public void handler(Message message) {
        if (!message.getFromUser().equals(JMessageClient.getMyInfo())) {
            switch (message.getContentType()) {
                case text:
                    //处理文字消息
                    TextContent textContent = (TextContent) message.getContent();
                    String content = textContent.getText();
                    MyMessage m = JSON.parseObject(content, MyMessage.class);
                    ALog.d(m);
                    switch (m.code) {
                        case Config.CODE_CLIENT_RESPOND:
                            Group.get().respond(message.getFromUser());
                            break;
                    }
                    break;
            }
        }
    }
}
