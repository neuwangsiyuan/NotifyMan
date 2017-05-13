package com.wsy.notifyman;

import android.app.Application;
import android.util.Log;

import com.hss01248.notifyutil.NotifyUtil;
import com.wsy.notifyman.common.MessageDispatcher;
import com.wsy.notifyman.common.SPHelper;
import com.wsy.notifyman.model.Apply;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Message;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by 思远 on 2017/5/6.
 */

public class App extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        NotifyUtil.init(this);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("notifyMan")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        JMessageClient.init(this);
        JMessageClient.setDebugMode(true);
        SPHelper.get().init(this,"notify");


        JMessageClient.registerEventReceiver(this);
    }


    public void onEventMainThread(MessageEvent event){
        Message msg = event.getMessage();
        MessageDispatcher.get().dispatcher(msg);

    }

    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Apply apply = realm.createObject(Apply.class);
                apply.applyReason = reason;
                apply.applyUser  = fromUsername;
                apply.status = Config.APPLY_NEW;
                apply.applyTime = System.currentTimeMillis();
                realm.commitTransaction();
                Log.d("TAG", "onEvent: "+apply);
                //...
                break;
            case invite_accepted://对方接收了你的好友邀请
                //...
                break;
            case invite_declined://对方拒绝了你的好友邀请
                //...
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                break;
            default:
                break;
        }
    }
}
