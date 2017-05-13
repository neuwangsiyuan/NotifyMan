package com.wsy.notifyman.server;

import com.alibaba.fastjson.JSON;
import com.wsy.notifyman.common.MessageHandler;
import com.wsy.notifyman.model.Apply;
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
        switch (message.getContentType()){
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) message.getContent();
                String content =textContent.getText();
                MyMessage m = JSON.parseObject(content, MyMessage.class);
                switch (m.code){
                    case 1:
                        String data =  m.toTarget(String.class);
                        MyMessage mm = new MyMessage();
                        mm.code = 2;
                        mm.data = "{a:1}";


                        Message msg =JMessageClient.createSingleTextMessage(message.getFromUser().getUserName(),mm.toJson());

                        JMessageClient.sendMessage(msg);
                        break;
                }
                break;
        }
    }
}
