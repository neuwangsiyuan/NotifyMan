package com.wsy.notifyman.client;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wsy.notifyman.Config;
import com.wsy.notifyman.R;
import com.wsy.notifyman.common.BaseActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by 思远 on 2017/5/7.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_splash);
        AndPermission.with(this)
                .permission(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .requestCode(100)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (Config.isServer) {
                            startActivity(new Intent(SplashActivity.this, com.wsy.notifyman.server.HomeActivity.class));
                        } else {
                            UserInfo userInfo = JMessageClient.getMyInfo();
                            if (userInfo == null) {
                                navToLogin();
                            } else {
                                //登录了
                                navToHome();
                            }
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                    }
                }).start();
    }

    private void navToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void navToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
