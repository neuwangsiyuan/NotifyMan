package com.wsy.notifyman.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wsy.notifyman.R;
import com.wsy.notifyman.common.BaseActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 思远 on 2017/5/7.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_splash);

        UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo == null) {
           navToLogin();
        } else {
            //登录了
            navToHome();
        }
    }

    private void navToLogin() {
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private void navToHome() {
        startActivity(new Intent(this,HomeActivity.class));finish();
    }
}
