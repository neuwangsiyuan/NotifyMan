package com.wsy.notifyman.common;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by 思远 on 2017/5/7.
 */

public interface MessageHandler {

    void handler(Message message);
}
