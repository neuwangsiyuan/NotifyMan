package com.wsy.notifyman.common;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.client.ClientMessageHandler;
import com.wsy.notifyman.server.ServerMessageHandler;

import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by 思远 on 2017/5/7.
 */

public class MessageDispatcher {

    private static MessageDispatcher handler;
    private MessageHandler messageHandler;

    private MessageDispatcher(){
        if(Config.isServer){
            messageHandler = new ServerMessageHandler();
        }else{
            messageHandler = new ClientMessageHandler();
        }
    }
    public static MessageDispatcher get(){
        if(handler == null)
            handler = new MessageDispatcher();
        return handler;
    }

    public void dispatcher(Message msg){
        messageHandler.handler(msg);
    }
}
