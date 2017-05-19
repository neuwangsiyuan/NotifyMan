package com.wsy.notifyman.client.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wsy.notifyman.R;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import dong.lan.base.ui.BaseActivity;

/**
 * Created by 思远 on 2017/5/7.
 */

public class RegisterActivity extends BaseActivity {
    private EditText username;
    private EditText password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_pwd);

        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(username.getText().toString(),password.getText().toString());
            }
        });
    }

    private void register(final String username, final String password) {
        if(TextUtils.isEmpty(username) ||TextUtils.isEmpty(password)){
            toast("用户名密码不能为空");
            return;
        }
        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    Intent intent = new Intent();
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    setResult(101,intent);
                    toast("为你自动登陆中");
                    finish();
                }else{
                    toast("注册失败："+i+""+s);
                }
            }
        });
    }
}
