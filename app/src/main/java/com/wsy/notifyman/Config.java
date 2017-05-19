package com.wsy.notifyman;

/**
 * Created by 思远 on 2017/5/7.
 */

public class Config {

    public static final int APPLY_NEW = 0;
    public static final int APPLY_AGREE = 1;
    public static final boolean isServer = true;
    public static final String API_KEY = "ee21464af578b344a60b0c91";
    public static final int MSG_TEXT = 0;
    public static final int CODE_SERVER_SEND = 10001;
    public static final int CODE_CLIENT_RESPOND = 10002;
    public static final long TIME_GAP = 1000 * 30; //30s的自动回复检测
}
