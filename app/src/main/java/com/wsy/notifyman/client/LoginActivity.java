package com.wsy.notifyman.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wsy.notifyman.R;
import com.wsy.notifyman.common.BaseActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 思远 on 2017/5/7.
 */

public class LoginActivity extends BaseActivity {


    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        username = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_pwd);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction(username.getText().toString(),password.getText().toString());
            }
        });

        findViewById(R.id.to_register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class),100);
            }
        });
    }

    private void navToHome() {
        startActivity(new Intent(this,HomeActivity.class));
    }

    private void loginAction(String username,String password) {
        if(TextUtils.isEmpty(username) ||TextUtils.isEmpty(password)){
            toast("用户名密码不能为空");
            return;
        }
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if(i == 0){
                    navToHome();
                }else{
                    toast("登陆失败："+i+""+s);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == 101){
            String username = data.getStringExtra("username");
            String pwd = data.getStringExtra("password");
            loginAction(username,pwd);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
