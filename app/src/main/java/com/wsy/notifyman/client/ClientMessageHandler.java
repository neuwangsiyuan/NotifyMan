package com.wsy.notifyman.client;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hss01248.notifyutil.NotifyUtil;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.MessageHandler;
import com.wsy.notifyman.model.LocalMessage;
import com.wsy.notifyman.model.MyMessage;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import io.realm.Realm;

/**
 * Created by 思远 on 2017/5/7.
 */

public class ClientMessageHandler implements MessageHandler {
    @Override
    public void handler(Message message) {
        Log.d("TAG", "handler: " + message.getContentType());
        //处理文字消息
        TextContent textContent = (TextContent) message.getContent();
        String content = textContent.getText();
        final MyMessage myMessage = JSON.parseObject(content, MyMessage.class);
        Log.d("TAG", "handler: " + myMessage);
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LocalMessage localMessage = realm.createObject(LocalMessage.class);
                localMessage.data = myMessage.toJson();
                localMessage.createTime = System.currentTimeMillis();
                Log.d("TAG", "execute: " + localMessage);
            }
        });

        NotifyUtil.buildSimple(message.getId(), R.mipmap.ic_launcher,message.getFromUser().getUserName(),
                myMessage.data,NotifyUtil.buildIntent(MessageDetailActivity.class)).show();

        switch (myMessage.code){

        }

    }
}
